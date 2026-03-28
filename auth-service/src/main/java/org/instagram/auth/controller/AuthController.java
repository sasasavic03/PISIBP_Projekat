package org.instagram.auth.controller;

import org.instagram.auth.dto.*;
import org.instagram.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody TokenRequest request) {
        String newToken = authService.refreshToken(request.getToken());
        return ResponseEntity.ok(new AuthResponse(newToken, null, null, null));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@Valid @RequestBody TokenRequest request) {
        boolean isValid = authService.validateToken(request.getToken());
        if (isValid) {
            return ResponseEntity.ok(new TokenValidationResponse(true, "Token is valid"));
        } else {
            return ResponseEntity.ok(new TokenValidationResponse(false, "Token is invalid or expired"));
        }
    }

}