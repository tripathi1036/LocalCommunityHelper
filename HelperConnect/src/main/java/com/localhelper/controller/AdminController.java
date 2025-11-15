package com.localhelper.controller;

import com.localhelper.dto.response.*;
import com.localhelper.entity.*;
import com.localhelper.security.UserDetailsImpl;
import com.localhelper.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Admin management APIs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
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
    
    // User Management
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get all users with pagination")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }
    
    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user by ID", description = "Get detailed information about a specific user")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", user));
    }
    
    @GetMapping("/users/role/{role}")
    @Operation(summary = "Get users by role", description = "Get users filtered by role")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersByRole(
            @PathVariable User.Role role,
            Pageable pageable) {
        Page<UserResponse> users = userService.getUsersByRole(role, pageable);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }
    
    @GetMapping("/users/search")
    @Operation(summary = "Search users", description = "Search users by username, email, or full name")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam String searchTerm,
            Pageable pageable) {
        Page<UserResponse> users = userService.searchUsers(searchTerm, pageable);
        return ResponseEntity.ok(ApiResponse.success("Users search completed successfully", users));
    }
    
    @PostMapping("/users/{userId}/activate")
    @Operation(summary = "Activate user", description = "Activate a user account")
    public ResponseEntity<ApiResponse<String>> activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully"));
    }
    
    @PostMapping("/users/{userId}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account")
    public ResponseEntity<ApiResponse<String>> deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully"));
    }
    
    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Delete user", description = "Delete a user account")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
    
    // Helper Management
    @GetMapping("/helpers")
    @Operation(summary = "Get all helpers", description = "Get all helpers with pagination")
    public ResponseEntity<ApiResponse<Page<HelperResponse>>> getAllHelpers(Pageable pageable) {
        Page<HelperResponse> helpers = helperService.getAllHelpers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Helpers retrieved successfully", helpers));
    }
    
    @GetMapping("/helpers/status/{status}")
    @Operation(summary = "Get helpers by status", description = "Get helpers filtered by status")
    public ResponseEntity<ApiResponse<Page<HelperResponse>>> getHelpersByStatus(
            @PathVariable Helper.HelperStatus status,
            Pageable pageable) {
        Page<HelperResponse> helpers = helperService.getHelpersByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Helpers retrieved successfully", helpers));
    }
    
    @PostMapping("/helpers/{helperId}/approve")
    @Operation(summary = "Approve helper", description = "Approve a helper registration")
    public ResponseEntity<ApiResponse<HelperResponse>> approveHelper(@PathVariable Long helperId) {
        HelperResponse helper = helperService.approveHelper(helperId);
        return ResponseEntity.ok(ApiResponse.success("Helper approved successfully", helper));
    }
    
    @PostMapping("/helpers/{helperId}/reject")
    @Operation(summary = "Reject helper", description = "Reject a helper registration")
    public ResponseEntity<ApiResponse<HelperResponse>> rejectHelper(
            @PathVariable Long helperId,
            @RequestParam String reason) {
        HelperResponse helper = helperService.rejectHelper(helperId, reason);
        return ResponseEntity.ok(ApiResponse.success("Helper rejected successfully", helper));
    }
    
    @PostMapping("/helpers/{helperId}/approve-kyc")
    @Operation(summary = "Approve KYC", description = "Approve helper's KYC documents")
    public ResponseEntity<ApiResponse<HelperResponse>> approveKyc(@PathVariable Long helperId) {
        HelperResponse helper = helperService.approveKyc(helperId);
        return ResponseEntity.ok(ApiResponse.success("KYC approved successfully", helper));
    }
    
    @PostMapping("/helpers/{helperId}/reject-kyc")
    @Operation(summary = "Reject KYC", description = "Reject helper's KYC documents")
    public ResponseEntity<ApiResponse<HelperResponse>> rejectKyc(
            @PathVariable Long helperId,
            @RequestParam String reason) {
        HelperResponse helper = helperService.rejectKyc(helperId, reason);
        return ResponseEntity.ok(ApiResponse.success("KYC rejected successfully", helper));
    }
    
    // Service Request Management
    @GetMapping("/service-requests")
    @Operation(summary = "Get all service requests", description = "Get all service requests with pagination")
    public ResponseEntity<ApiResponse<Page<ServiceRequestResponse>>> getAllServiceRequests(Pageable pageable) {
        Page<ServiceRequestResponse> serviceRequests = serviceRequestService.getServiceRequestsByStatus(null, pageable);
        return ResponseEntity.ok(ApiResponse.success("Service requests retrieved successfully", serviceRequests));
    }
    
    @GetMapping("/service-requests/status/{status}")
    @Operation(summary = "Get service requests by status", description = "Get service requests filtered by status")
    public ResponseEntity<ApiResponse<Page<ServiceRequestResponse>>> getServiceRequestsByStatus(
            @PathVariable ServiceRequest.RequestStatus status,
            Pageable pageable) {
        Page<ServiceRequestResponse> serviceRequests = serviceRequestService.getServiceRequestsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Service requests retrieved successfully", serviceRequests));
    }
    
    // Payment Management
    @GetMapping("/payments")
    @Operation(summary = "Get all payments", description = "Get all payments with pagination")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getAllPayments(Pageable pageable) {
        Page<PaymentResponse> payments = paymentService.getPaymentsByStatus(null, pageable);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }
    
    @GetMapping("/payments/status/{status}")
    @Operation(summary = "Get payments by status", description = "Get payments filtered by status")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getPaymentsByStatus(
            @PathVariable Payment.PaymentStatus status,
            Pageable pageable) {
        Page<PaymentResponse> payments = paymentService.getPaymentsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }
    
    @PostMapping("/payments/{paymentId}/refund")
    @Operation(summary = "Refund payment", description = "Process a payment refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(
            @PathVariable Long paymentId,
            @RequestParam String reason) {
        PaymentResponse payment = paymentService.refundPayment(paymentId, reason);
        return ResponseEntity.ok(ApiResponse.success("Payment refunded successfully", payment));
    }
    
    // Review Management
    @GetMapping("/reviews")
    @Operation(summary = "Get all reviews", description = "Get all reviews with pagination")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllReviews(Pageable pageable) {
        Page<ReviewResponse> reviews = reviewService.getReviewsWithComments(pageable);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }
    
    @PostMapping("/reviews/{reviewId}/hide")
    @Operation(summary = "Hide review", description = "Hide a review from public view")
    public ResponseEntity<ApiResponse<ReviewResponse>> hideReview(@PathVariable Long reviewId) {
        ReviewResponse review = reviewService.hideReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success("Review hidden successfully", review));
    }
    
    @PostMapping("/reviews/{reviewId}/show")
    @Operation(summary = "Show review", description = "Show a hidden review")
    public ResponseEntity<ApiResponse<ReviewResponse>> showReview(@PathVariable Long reviewId) {
        ReviewResponse review = reviewService.showReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success("Review shown successfully", review));
    }
    
    // Complaint Management
    @GetMapping("/complaints")
    @Operation(summary = "Get all complaints", description = "Get all complaints with pagination")
    public ResponseEntity<ApiResponse<Page<ComplaintResponse>>> getAllComplaints(Pageable pageable) {
        Page<ComplaintResponse> complaints = complaintService.getComplaintsByStatus(null, pageable);
        return ResponseEntity.ok(ApiResponse.success("Complaints retrieved successfully", complaints));
    }
    
    @GetMapping("/complaints/status/{status}")
    @Operation(summary = "Get complaints by status", description = "Get complaints filtered by status")
    public ResponseEntity<ApiResponse<Page<ComplaintResponse>>> getComplaintsByStatus(
            @PathVariable Complaint.ComplaintStatus status,
            Pageable pageable) {
        Page<ComplaintResponse> complaints = complaintService.getComplaintsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Complaints retrieved successfully", complaints));
    }
    
    @GetMapping("/complaints/search")
    @Operation(summary = "Search complaints", description = "Search complaints by subject or description")
    public ResponseEntity<ApiResponse<Page<ComplaintResponse>>> searchComplaints(
            @RequestParam String searchTerm,
            Pageable pageable) {
        Page<ComplaintResponse> complaints = complaintService.searchComplaints(searchTerm, pageable);
        return ResponseEntity.ok(ApiResponse.success("Complaints search completed successfully", complaints));
    }
    
    @PostMapping("/complaints/{complaintId}/resolve")
    @Operation(summary = "Resolve complaint", description = "Resolve a complaint with admin response")
    public ResponseEntity<ApiResponse<ComplaintResponse>> resolveComplaint(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long complaintId,
            @RequestParam String response) {
        ComplaintResponse complaint = complaintService.resolveComplaint(complaintId, currentUser.getId(), response);
        return ResponseEntity.ok(ApiResponse.success("Complaint resolved successfully", complaint));
    }
    
    @PostMapping("/complaints/{complaintId}/close")
    @Operation(summary = "Close complaint", description = "Close a complaint with admin response")
    public ResponseEntity<ApiResponse<ComplaintResponse>> closeComplaint(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long complaintId,
            @RequestParam String response) {
        ComplaintResponse complaint = complaintService.closeComplaint(complaintId, currentUser.getId(), response);
        return ResponseEntity.ok(ApiResponse.success("Complaint closed successfully", complaint));
    }
    
    @PostMapping("/complaints/{complaintId}/priority")
    @Operation(summary = "Update complaint priority", description = "Update the priority of a complaint")
    public ResponseEntity<ApiResponse<ComplaintResponse>> updateComplaintPriority(
            @PathVariable Long complaintId,
            @RequestParam Complaint.Priority priority) {
        ComplaintResponse complaint = complaintService.updateComplaintPriority(complaintId, priority);
        return ResponseEntity.ok(ApiResponse.success("Complaint priority updated successfully", complaint));
    }
    
    // Analytics and Reports
    @GetMapping("/analytics/dashboard")
    @Operation(summary = "Get admin dashboard analytics", description = "Get comprehensive analytics for admin dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminDashboard() {
        Map<String, Object> analytics = new HashMap<>();
        
        // User statistics
        analytics.put("totalUsers", userService.getTotalUserCount());
        analytics.put("totalHelpers", helperService.getTotalHelperCount());
        analytics.put("pendingHelpers", helperService.getHelperCountByStatus(Helper.HelperStatus.PENDING));
        analytics.put("approvedHelpers", helperService.getHelperCountByStatus(Helper.HelperStatus.APPROVED));
        
        // Service request statistics
        analytics.put("totalServiceRequests", serviceRequestService.getTotalServiceRequestCount());
        analytics.put("pendingRequests", serviceRequestService.getServiceRequestCountByStatus(ServiceRequest.RequestStatus.PENDING));
        analytics.put("completedRequests", serviceRequestService.getServiceRequestCountByStatus(ServiceRequest.RequestStatus.COMPLETED));
        
        // Payment statistics
        analytics.put("totalPayments", paymentService.getTotalPaymentCount());
        analytics.put("completedPayments", paymentService.getPaymentCountByStatus(Payment.PaymentStatus.COMPLETED));
        analytics.put("failedPayments", paymentService.getPaymentCountByStatus(Payment.PaymentStatus.FAILED));
        
        // Review statistics
        analytics.put("totalReviews", reviewService.getTotalReviewCount());
        
        // Complaint statistics
        analytics.put("totalComplaints", complaintService.getTotalComplaintCount());
        analytics.put("openComplaints", complaintService.getComplaintCountByStatus(Complaint.ComplaintStatus.OPEN));
        analytics.put("resolvedComplaints", complaintService.getComplaintCountByStatus(Complaint.ComplaintStatus.RESOLVED));
        
        // Revenue statistics
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now();
        analytics.put("monthlyRevenue", paymentService.calculateTotalRevenueBetween(startOfMonth, endOfMonth));
        
        return ResponseEntity.ok(ApiResponse.success("Admin analytics retrieved successfully", analytics));
    }
    
    @GetMapping("/analytics/revenue")
    @Operation(summary = "Get revenue analytics", description = "Get revenue analytics for a specific time period")
    public ResponseEntity<ApiResponse<BigDecimal>> getRevenueAnalytics(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        BigDecimal revenue = paymentService.calculateTotalRevenueBetween(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Revenue analytics retrieved successfully", revenue));
    }
}