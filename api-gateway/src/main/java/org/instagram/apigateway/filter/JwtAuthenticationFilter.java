package org.instagram.apigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instagram.apigateway.config.PublicEndpointsProperties;
import org.instagram.apigateway.util.HeaderExtractor;
import org.instagram.apigateway.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map;

@Component
@Order(1)
public class JwtAuthenticationFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final JwtTokenProvider jwtTokenProvider;
    private final HeaderExtractor headerExtractor;
    private final List<String> publicEndpoints;

    private static final List<String> DEFAULT_PUBLIC_ENDPOINTS = Arrays.asList(
            "/health",
            "/api/health",
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh",
            "/api/auth/validate"
    );

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   HeaderExtractor headerExtractor,
                                   PublicEndpointsProperties publicEndpointsProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.headerExtractor = headerExtractor;

        this.publicEndpoints = new ArrayList<>(publicEndpointsProperties.getPublicEndpoints());
        if (this.publicEndpoints.isEmpty()) {
            logger.warn("No public endpoints configured, using default list");
            this.publicEndpoints.addAll(DEFAULT_PUBLIC_ENDPOINTS);
        }
        
        logger.info("JWT Filter initialized with {} public endpoints: {}", 
                   this.publicEndpoints.size(), this.publicEndpoints);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().name();

        if (isPublicEndpoint(requestPath)) {
            logger.debug("Public endpoint accessed: {} {}", method, requestPath);
            return chain.filter(exchange);
        }

        try {
            String token = headerExtractor.extractBearerToken(exchange);

            if (token == null || token.trim().isEmpty()) {
                logger.warn("Authorization header is missing for request: {} {}", method, requestPath);
                return onError(exchange, "Authorization header is missing", "MISSING_AUTHORIZATION", HttpStatus.UNAUTHORIZED);
            }

            logger.debug("Token received for request: {} {}, validating...", method, requestPath);

            if (!jwtTokenProvider.validateToken(token)) {
                logger.warn("Invalid or expired JWT token for request: {} {}", method, requestPath);
                return onError(exchange, "Invalid or expired JWT token", "INVALID_TOKEN", HttpStatus.UNAUTHORIZED);
            }

            String username = jwtTokenProvider.getUsernameFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            if (username == null || userId == null) {
                logger.warn("Invalid token claims for request: {} {}", method, requestPath);
                return onError(exchange, "Invalid token claims", "INVALID_CLAIMS", HttpStatus.UNAUTHORIZED);
            }

            logger.debug("Token validated successfully for user: {} (ID: {}), request: {} {}", 
                        username, userId, method, requestPath);

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-Username", username)
                            .header("X-User-Id", String.valueOf(userId))
                            .header("X-Token-Valid", "true")
                            .header("Authorization", "Bearer " + token)  // Preserve token for downstream
                            .build())
                    .build();

            logger.debug("Request headers enriched with user context");
            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            logger.error("Unexpected authentication error for request: {} {}", method, requestPath, e);
            return onError(exchange, "Internal server error", "AUTH_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isPublicEndpoint(String requestPath) {
        for (String endpoint : publicEndpoints) {
            if (requestPath.equals(endpoint) || requestPath.startsWith(endpoint)) {
                logger.debug("Public endpoint matched: {} for request: {}", endpoint, requestPath);
                return true;
            }
        }
        logger.debug("Request path {} is not a public endpoint", requestPath);
        return false;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, String errorCode, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("X-Error-Code", errorCode);

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", status.value());
        errorBody.put("error", errorCode);
        errorBody.put("message", message);
        errorBody.put("timestamp", LocalDateTime.now());

        logger.warn("Error response: status={}, code={}, message={}", status.value(), errorCode, message);

        try {
            String jsonErrorBody = objectMapper.writeValueAsString(errorBody);
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory()
                            .wrap(jsonErrorBody.getBytes(StandardCharsets.UTF_8)))
            );
        } catch (Exception e) {
            logger.error("Error serializing error response: {}", e.getMessage(), e);
            // Fallback to plain text if serialization fails
            String fallbackError = String.format("status=%d&error=%s", status.value(), errorCode);
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory()
                            .wrap(fallbackError.getBytes(StandardCharsets.UTF_8)))
            );
        }
    }
}



