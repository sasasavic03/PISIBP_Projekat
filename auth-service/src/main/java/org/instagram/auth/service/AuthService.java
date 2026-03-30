package org.instagram.auth.service;

import org.instagram.auth.client.UserServiceClient;
import org.instagram.auth.dto.*;
import org.instagram.auth.entity.User;
import org.instagram.auth.exception.InvalidCredentialsException;
import org.instagram.auth.exception.UserAlreadyExistsException;
import org.instagram.auth.repository.UserRepository;
import org.instagram.auth.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    @Transactional
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

        Long userProfileId;
        try {
            CreateUserRequest profileRequest = new CreateUserRequest();
            profileRequest.setUsername(user.getUsername());
            profileRequest.setEmail(user.getEmail());
            UserProfileResponse profileResponse = userServiceClient.createUserProfile(profileRequest);
            userProfileId = profileResponse.getId();
        } catch (Exception e) {
            logger.error("Failed to create user profile in user-service for user: {}", user.getUsername(), e);
            throw new RuntimeException("Registration failed - could not create user profile");
        }

        String token = jwtService.generateToken(user.getUsername(), userProfileId);

        // Fetch avatar from user profile
        String avatar = null;
        try {
            UserProfileResponse profileResponse = userServiceClient.getUserById(userProfileId);
            if (profileResponse != null) {
                avatar = profileResponse.getProfilePictureUrl();
            }
        } catch (Exception e) {
            logger.warn("Failed to fetch avatar for newly registered user: {}", user.getUsername());
        }

        return new AuthResponse(token, userProfileId, user.getUsername(), avatar);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getUsername())
                .or(() -> userRepository.findByUsername(request.getUsername()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Long canonicalUserId = resolveCanonicalUserId(user);
        String token = jwtService.generateToken(user.getUsername(), canonicalUserId);

        // Fetch avatar from user profile
        String avatar = null;
        try {
            UserProfileResponse profileResponse = userServiceClient.getUserById(canonicalUserId);
            if (profileResponse != null) {
                avatar = profileResponse.getProfilePictureUrl();
            }
        } catch (Exception e) {
            logger.warn("Failed to fetch avatar for user during login: {}", user.getUsername());
        }

        return new AuthResponse(token, canonicalUserId, user.getUsername(), avatar);
    }

    private Long resolveCanonicalUserId(User user) {
        try {
            UserProfileResponse profile = userServiceClient.getUserByUsername(user.getUsername());
            if (profile != null && profile.getId() != null) {
                return profile.getId();
            }
        } catch (Exception e) {
            logger.warn("Failed to resolve profile id for user {}. Falling back to auth id {}", user.getUsername(), user.getId());
        }
        return user.getId();
    }

    public boolean validateToken(String token) {
        try {
            return jwtService.validateToken(token);
        } catch (Exception e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String refreshToken(String token) {
        if (!validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        return jwtService.refreshToken(token);
    }
}