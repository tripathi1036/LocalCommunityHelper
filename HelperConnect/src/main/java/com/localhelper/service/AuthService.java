package com.localhelper.service;

import com.localhelper.dto.request.LoginRequest;
import com.localhelper.dto.request.UserRegistrationRequest;
import com.localhelper.dto.response.JwtAuthenticationResponse;
import com.localhelper.dto.response.UserResponse;
import com.localhelper.entity.User;
import com.localhelper.exception.BusinessException;
import com.localhelper.repository.UserRepository;
import com.localhelper.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtProvider tokenProvider;
    
    @Autowired
    private UserService userService;
    
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        logger.info("Authenticating user: {}", loginRequest.getUsernameOrEmail());
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken(authentication);
        
        User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getUsernameOrEmail()
        ).orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));
        
        if (!user.getIsActive()) {
            throw new BusinessException("ACCOUNT_DISABLED", "Your account has been disabled. Please contact support.");
        }
        
        UserResponse userResponse = new UserResponse(user);
        
        logger.info("User authenticated successfully: {}", user.getUsername());
        return new JwtAuthenticationResponse(jwt, userResponse);
    }
    
    public UserResponse registerUser(UserRegistrationRequest signUpRequest) {
        logger.info("Registering new user: {}", signUpRequest.getUsername());
        
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BusinessException("DUPLICATE_USERNAME", "Username is already taken!");
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BusinessException("DUPLICATE_EMAIL", "Email address is already in use!");
        }
        
        // Create user account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFullName(),
                signUpRequest.getPhone()
        );
        
        user.setAddress(signUpRequest.getAddress());
        user.setRole(User.Role.USER);
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        
        logger.info("User registered successfully: {}", savedUser.getUsername());
        return new UserResponse(savedUser);
    }
    
    public UserResponse registerAdmin(UserRegistrationRequest signUpRequest) {
        logger.info("Registering new admin: {}", signUpRequest.getUsername());
        
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BusinessException("DUPLICATE_USERNAME", "Username is already taken!");
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BusinessException("DUPLICATE_EMAIL", "Email address is already in use!");
        }
        
        // Create admin account
        User admin = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFullName(),
                signUpRequest.getPhone()
        );
        
        admin.setAddress(signUpRequest.getAddress());
        admin.setRole(User.Role.ADMIN);
        admin.setIsActive(true);
        
        User savedAdmin = userRepository.save(admin);
        
        logger.info("Admin registered successfully: {}", savedAdmin.getUsername());
        return new UserResponse(savedAdmin);
    }
    
    @Transactional(readOnly = true)
    public Boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    @Transactional(readOnly = true)
    public Boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    
    public void resetPassword(String email, String newPassword) {
        logger.info("Resetting password for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "No user found with email: " + email));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        logger.info("Password reset successfully for user: {}", user.getUsername());
    }
    
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        logger.info("Changing password for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException("INVALID_PASSWORD", "Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        logger.info("Password changed successfully for user: {}", user.getUsername());
    }
    
    public void activateAccount(Long userId) {
        logger.info("Activating account for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        user.setIsActive(true);
        userRepository.save(user);
        
        logger.info("Account activated successfully for user: {}", user.getUsername());
    }
    
    public void deactivateAccount(Long userId) {
        logger.info("Deactivating account for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        user.setIsActive(false);
        userRepository.save(user);
        
        logger.info("Account deactivated successfully for user: {}", user.getUsername());
    }
}