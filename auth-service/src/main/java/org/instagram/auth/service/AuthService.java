package org.instagram.auth.service;

import org.instagram.auth.client.UserServiceClient;
import org.instagram.auth.dto.*;
import org.instagram.auth.entity.User;
import org.instagram.auth.exception.InvalidCredentialsException;
import org.instagram.auth.exception.UserAlreadyExistsException;
import org.instagram.auth.repository.UserRepository;
import org.instagram.auth.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UserServiceClient userServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        try {
            CreateUserRequest profileRequest = new CreateUserRequest();
            profileRequest.setId(user.getId());
            profileRequest.setUsername(user.getUsername());
            profileRequest.setEmail(user.getEmail());
            userServiceClient.createUserProfile(profileRequest);
        } catch (Exception e) {
            System.err.println("Failed to create profile in user-service: " + e.getMessage());
        }

        String token = jwtService.generateToken(user.getUsername(), user.getId());

        return new AuthResponse(token, user.getId(), user.getUsername(), null);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getUsername())
                .or(() -> userRepository.findByUsername(request.getUsername()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getId());

        return new AuthResponse(token, user.getId(), user.getUsername(), null);
    }
}