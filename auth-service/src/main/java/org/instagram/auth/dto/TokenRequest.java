package org.instagram.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenRequest {

    @NotBlank(message = "Token cannot be blank")
    private String token;

    public TokenRequest() {}

    public TokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

