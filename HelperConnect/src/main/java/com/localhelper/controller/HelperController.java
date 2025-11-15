package com.localhelper.controller;

import com.localhelper.dto.request.HelperRegistrationRequest;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/helper")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Helper", description = "Helper management APIs")
public class HelperController {
    
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
    
    @PostMapping("/register")
    @Operation(summary = "Register as helper", description = "Register current user as a helper with KYC documents")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HelperResponse>> registerAsHelper(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody HelperRegistrationRequest request) {
        HelperResponse response = helperService.registerHelper(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Helper registration submitted successfully", response));
    }
    
    @GetMapping("/profile")
    @Operation(summary = "Get helper profile", description = "Get current helper's profile information")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HelperResponse>> getHelperProfile(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        HelperResponse helper = helperService.getHelperByUserId(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Helper profile retrieved successfully", helper));
    }
    
    @PutMapping("/profile")
    @Operation(summary = "Update helper profile", description = "Update helper profile information")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HelperResponse>> updateHelperProfile(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody HelperRegistrationRequest request) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        HelperResponse response = helperService.updateHelperProfile(currentHelper.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Helper profile updated successfully", response));
    }
    
    @PutMapping("/availability")
    @Operation(summary = "Update availability", description = "Update helper availability status")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HelperResponse>> updateAvailability(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam Boolean isAvailable) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        HelperResponse response = helperService.updateAvailability(currentHelper.getId(), isAvailable);
        return ResponseEntity.ok(ApiResponse.success("Availability updated successfully", response));
    }
    
    // Service Request Management
    @GetMapping("/service-requests")
    @Operation(summary = "Get helper service requests", description = "Get all service requests assigned to current helper")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ServiceRequestResponse>>> getHelperServiceRequests(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        Page<ServiceRequestResponse> serviceRequests = serviceRequestService.getServiceRequestsByHelperId(currentHelper.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Service requests retrieved successfully", serviceRequests));
    }
    
    @PostMapping("/service-requests/{requestId}/accept")
    @Operation(summary = "Accept service request", description = "Accept a pending service request")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> acceptServiceRequest(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long requestId) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        ServiceRequestResponse response = serviceRequestService.acceptServiceRequest(requestId, currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Service request accepted successfully", response));
    }
    
    @PostMapping("/service-requests/{requestId}/reject")
    @Operation(summary = "Reject service request", description = "Reject a pending service request")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> rejectServiceRequest(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long requestId,
            @RequestParam String reason) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        ServiceRequestResponse response = serviceRequestService.rejectServiceRequest(requestId, currentHelper.getId(), reason);
        return ResponseEntity.ok(ApiResponse.success("Service request rejected successfully", response));
    }
    
    @PostMapping("/service-requests/{requestId}/start")
    @Operation(summary = "Start service", description = "Start working on an accepted service request")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> startService(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long requestId) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        ServiceRequestResponse response = serviceRequestService.startService(requestId, currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Service started successfully", response));
    }
    
    @PostMapping("/service-requests/{requestId}/complete")
    @Operation(summary = "Complete service", description = "Mark service request as completed")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> completeService(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long requestId) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        ServiceRequestResponse response = serviceRequestService.completeService(requestId, currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Service completed successfully", response));
    }
    
    // Earnings and Payments
    @GetMapping("/earnings")
    @Operation(summary = "Get helper earnings", description = "Get current helper's total earnings")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BigDecimal>> getHelperEarnings(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        BigDecimal earnings = paymentService.calculateTotalEarningsByHelperId(currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Earnings retrieved successfully", earnings));
    }
    
    @GetMapping("/payments")
    @Operation(summary = "Get helper payments", description = "Get all payments received by current helper")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getHelperPayments(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        Page<PaymentResponse> payments = paymentService.getPaymentsByHelperId(currentHelper.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }
    
    // Reviews
    @GetMapping("/reviews")
    @Operation(summary = "Get helper reviews", description = "Get all reviews for current helper")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getHelperReviews(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        Page<ReviewResponse> reviews = reviewService.getReviewsByHelperId(currentHelper.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }
    
    @GetMapping("/reviews/rating")
    @Operation(summary = "Get helper rating", description = "Get current helper's average rating")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BigDecimal>> getHelperRating(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        BigDecimal rating = reviewService.getAverageRatingByHelperId(currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Rating retrieved successfully", rating));
    }
    
    // Complaints
    @GetMapping("/complaints")
    @Operation(summary = "Get complaints against helper", description = "Get all complaints filed against current helper")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ComplaintResponse>>> getComplaintsAgainstHelper(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            Pageable pageable) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        Page<ComplaintResponse> complaints = complaintService.getComplaintsByHelperId(currentHelper.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Complaints retrieved successfully", complaints));
    }
    
    // Analytics
    @GetMapping("/analytics/requests-count")
    @Operation(summary = "Get service requests count", description = "Get total service requests count for current helper")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getServiceRequestsCount(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        Long count = serviceRequestService.getServiceRequestCountByHelperId(currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Service requests count retrieved successfully", count));
    }
    
    @GetMapping("/analytics/reviews-count")
    @Operation(summary = "Get reviews count", description = "Get total reviews count for current helper")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getReviewsCount(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        Long count = reviewService.getReviewCountByHelperId(currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Reviews count retrieved successfully", count));
    }
    
    @GetMapping("/analytics/complaints-count")
    @Operation(summary = "Get complaints count", description = "Get total complaints count against current helper")
    @PreAuthorize("hasRole('HELPER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getComplaintsCount(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        HelperResponse currentHelper = helperService.getHelperByUserId(currentUser.getId());
        Long count = complaintService.getComplaintCountByHelperId(currentHelper.getId());
        return ResponseEntity.ok(ApiResponse.success("Complaints count retrieved successfully", count));
    }
}