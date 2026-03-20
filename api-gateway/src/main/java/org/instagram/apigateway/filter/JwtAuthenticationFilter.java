package org.instagram.apigateway.filter;

import org.instagram.apigateway.util.HeaderExtractor;
import org.instagram.apigateway.util.JwtTokenProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final HeaderExtractor headerExtractor;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/health",
            "/health"
    };

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   HeaderExtractor headerExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.headerExtractor = headerExtractor;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();

        if (isPublicEndpoint(requestPath)) {
            return chain.filter(exchange);
        }

        try {
            String token = headerExtractor.extractBearerToken(exchange);

            if (token == null) {
                return onError(exchange, "Authorization header is missing", "MISSING_AUTHORIZATION");
            }

            if (!jwtTokenProvider.validateToken(token)) {
                return onError(exchange, "Invalid JWT token", "INVALID_TOKEN");
            }

            if (jwtTokenProvider.isTokenExpired(token)) {
                return onError(exchange, "JWT token has expired", "EXPIRED_TOKEN");
            }

            String username = jwtTokenProvider.getUsernameFromToken(token);

            ServerWebExchange modifiedExchange = headerExtractor.addRequestHeader(
                    exchange,
                    "X-Username",
                    username
            );

            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            return onError(exchange, "Authentication error", "AUTH_ERROR");
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

    private Mono<Void> onError(ServerWebExchange exchange, String message, String errorCode) {
        exchange.getResponse().setStatusCode(
                org.springframework.http.HttpStatus.UNAUTHORIZED
        );
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String errorBody = String.format(
                "{\"status\":401,\"error\":\"%s\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                errorCode,
                message,
                java.time.LocalDateTime.now()
        );

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory()
                        .wrap(errorBody.getBytes()))
        );
    }
}
