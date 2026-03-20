package org.instagram.apigateway.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class HeaderExtractor {

    public String extractBearerToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public ServerWebExchange addRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header(name, value)
                        .build())
                .build();
    }
}
