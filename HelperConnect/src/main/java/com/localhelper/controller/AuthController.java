package com.localhelper.controller;

import com.localhelper.dto.request.LoginRequest;
import com.localhelper.dto.request.UserRegistrationRequest;
import com.localhelper.dto.response.ApiResponse;
import com.localhelper.dto.response.JwtAuthenticationResponse;
import com.localhelper.dto.response.UserResponse;
import com.localhelper.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("User authenticated successfully", response));
    }
    
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody UserRegistrationRequest signUpRequest) {
        UserResponse userResponse = authService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", userResponse));
    }
    
    @PostMapping("/register-admin")
    @Operation(summary = "Admin registration", description = "Register a new admin account")
    public ResponseEntity<ApiResponse<UserResponse>> registerAdmin(@Valid @RequestBody UserRegistrationRequest signUpRequest) {
        UserResponse userResponse = authService.registerAdmin(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Admin registered successfully", userResponse));
    }
    
    @GetMapping("/check-username")
    @Operation(summary = "Check username availability", description = "Check if username is available for registration")
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameAvailability(@RequestParam String username) {
        Boolean isAvailable = authService.isUsernameAvailable(username);
        String message = isAvailable ? "Username is available" : "Username is already taken";
        return ResponseEntity.ok(ApiResponse.success(message, isAvailable));
    }
    
    @GetMapping("/check-email")
    @Operation(summary = "Check email availability", description = "Check if email is available for registration")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailability(@RequestParam String email) {
        Boolean isAvailable = authService.isEmailAvailable(email);
        String message = isAvailable ? "Email is available" : "Email is already registered";
        return ResponseEntity.ok(ApiResponse.success(message, isAvailable));
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset user password using email")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        authService.resetPassword(email, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
    }
    
    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change user password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestParam Long userId,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        authService.changePassword(userId, currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }
}