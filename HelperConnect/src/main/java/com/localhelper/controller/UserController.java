package com.localhelper.controller;

import com.localhelper.dto.request.*;
import com.localhelper.dto.response.*;
import com.localhelper.security.UserDetailsImpl;
import com.localhelper.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "User management APIs")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private HelperService helperService;
    
    @Autowired
    private ServiceRequestService serviceRequestService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private ComplaintService complaintService;
    
    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Get current user's profile information")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        UserResponse userResponse = userService.getUserById(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", userResponse));
    }
    
    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Update current user's profile information")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody UserRegistrationRequest request) {
        UserResponse userResponse = userService.updateUser(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", userResponse));
    }
    
    // Helper-related endpoints for users
    @GetMapping("/helpers")
    @Operation(summary = "Search helpers", description = "Search available helpers by service type")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<HelperResponse>>> searchHelpers(
            @RequestParam(required = false) String serviceType,
            Pageable pageable) {
        Page<HelperResponse> helpers;
        if (serviceType != null && !serviceType.trim().isEmpty()) {
            helpers = helperService.searchHelpers(serviceType, pageable);
        } else {
            helpers = helperService.getAvailableHelpers(pageable);
        }
        return ResponseEntity.ok(ApiResponse.success("Helpers retrieved successfully", helpers));
    }
    
    @GetMapping("/helpers/{helperId}")
    @Operation(summary = "Get helper details", description = "Get detailed information about a specific helper")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HelperResponse>> getHelper(@PathVariable Long helperId) {
        HelperResponse helper = helperService.getHelperById(helperId);
        return ResponseEntity.ok(ApiResponse.success("Helper details retrieved successfully", helper));
    }
    
    // Service Request endpoints
    @PostMapping("/service-requests")
    @Operation(summary = "Create service request", description = "Create a new service request")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> createServiceRequest(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody ServiceRequestRequest request) {
        ServiceRequestResponse response = serviceRequestService.createServiceRequest(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Service request created successfully", response));
    }
    
    @GetMapping("/service-requests")
    @Operation(summary = "Get user service requests", description = "Get all service requests for current user")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ServiceRequestResponse>>> getUserServiceRequests(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        Page<ServiceRequestResponse> serviceRequests = serviceRequestService.getServiceRequestsByUserId(currentUser.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Service requests retrieved successfully", serviceRequests));
    }
    
    @GetMapping("/service-requests/{requestId}")
    @Operation(summary = "Get service request details", description = "Get detailed information about a specific service request")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> getServiceRequest(@PathVariable Long requestId) {
        ServiceRequestResponse response = serviceRequestService.getServiceRequestById(requestId);
        return ResponseEntity.ok(ApiResponse.success("Service request details retrieved successfully", response));
    }
    
    @PutMapping("/service-requests/{requestId}")
    @Operation(summary = "Update service request", description = "Update a pending service request")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> updateServiceRequest(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long requestId,
            @Valid @RequestBody ServiceRequestRequest request) {
        ServiceRequestResponse response = serviceRequestService.updateServiceRequest(requestId, currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Service request updated successfully", response));
    }
    
    @PostMapping("/service-requests/{requestId}/cancel")
    @Operation(summary = "Cancel service request", description = "Cancel a service request")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> cancelServiceRequest(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long requestId) {
        ServiceRequestResponse response = serviceRequestService.cancelServiceRequest(requestId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Service request cancelled successfully", response));
    }
    
    // Payment endpoints
    @PostMapping("/payments")
    @Operation(summary = "Create payment", description = "Create a payment for a completed service")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment created successfully", response));
    }
    
    @PostMapping("/payments/{paymentId}/process")
    @Operation(summary = "Process payment", description = "Process a pending payment")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@PathVariable Long paymentId) {
        PaymentResponse response = paymentService.processPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", response));
    }
    
    @GetMapping("/payments")
    @Operation(summary = "Get user payments", description = "Get all payments for current user")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getUserPayments(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        Page<PaymentResponse> payments = paymentService.getPaymentsByUserId(currentUser.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }
    
    // Review endpoints
    @PostMapping("/reviews")
    @Operation(summary = "Create review", description = "Create a review for a completed service")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.createReview(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review created successfully", response));
    }
    
    @GetMapping("/reviews")
    @Operation(summary = "Get user reviews", description = "Get all reviews created by current user")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getUserReviews(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        Page<ReviewResponse> reviews = reviewService.getReviewsByUserId(currentUser.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }
    
    @PutMapping("/reviews/{reviewId}")
    @Operation(summary = "Update review", description = "Update an existing review")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.updateReview(reviewId, currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Review updated successfully", response));
    }
    
    @DeleteMapping("/reviews/{reviewId}")
    @Operation(summary = "Delete review", description = "Delete an existing review")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully"));
    }
    
    // Complaint endpoints
    @PostMapping("/complaints")
    @Operation(summary = "Create complaint", description = "Create a new complaint")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ComplaintResponse>> createComplaint(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody ComplaintRequest request) {
        ComplaintResponse response = complaintService.createComplaint(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Complaint created successfully", response));
    }
    
    @GetMapping("/complaints")
    @Operation(summary = "Get user complaints", description = "Get all complaints created by current user")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ComplaintResponse>>> getUserComplaints(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        Page<ComplaintResponse> complaints = complaintService.getComplaintsByUserId(currentUser.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Complaints retrieved successfully", complaints));
    }
    
    @GetMapping("/complaints/{complaintId}")
    @Operation(summary = "Get complaint details", description = "Get detailed information about a specific complaint")
    @PreAuthorize("hasRole('USER') or hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ComplaintResponse>> getComplaint(@PathVariable Long complaintId) {
        ComplaintResponse response = complaintService.getComplaintById(complaintId);
        return ResponseEntity.ok(ApiResponse.success("Complaint details retrieved successfully", response));
    }
}