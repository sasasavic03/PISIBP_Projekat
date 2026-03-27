package org.instagram.apigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instagram.apigateway.util.HeaderExtractor;
import org.instagram.apigateway.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(1)
public class JwtAuthenticationFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final HeaderExtractor headerExtractor;
    private final ObjectMapper objectMapper;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/health",
            "/api/health",
            "/actuator",
            "/actuator/health"
    };

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   HeaderExtractor headerExtractor,
                                   ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.headerExtractor = headerExtractor;
        this.objectMapper = objectMapper;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN";

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
                logger.warn("Invalid JWT token for request: {} {}", method, requestPath);
                return onError(exchange, "Invalid JWT token", "INVALID_TOKEN", HttpStatus.UNAUTHORIZED);
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
                            .header("Authorization", "Bearer " + token)
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
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (requestPath.startsWith(endpoint)) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, String errorCode, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("X-Error-Code", errorCode);

        try {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", status.value());
            errorResponse.put("error", errorCode);
            errorResponse.put("message", message);
            errorResponse.put("timestamp", LocalDateTime.now());

            String errorBody = objectMapper.writeValueAsString(errorResponse);

            logger.warn("Error response: status={}, code={}, message={}", status.value(), errorCode, message);

            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory()
                            .wrap(errorBody.getBytes(StandardCharsets.UTF_8)))
            );
        } catch (Exception e) {
            logger.error("Error serializing error response", e);
            String fallbackErrorBody = "{\"status\":" + status.value() + ",\"error\":\"" + errorCode + "\"}";
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory()
                            .wrap(fallbackErrorBody.getBytes(StandardCharsets.UTF_8)))
            );
        }
    }
}



