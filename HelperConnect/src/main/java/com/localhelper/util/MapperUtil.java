package com.localhelper.util;

import com.localhelper.dto.response.*;
import com.localhelper.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtil {
    
    public UserResponse toUserResponse(User user) {
        return new UserResponse(user);
    }
    
    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
    
    public HelperResponse toHelperResponse(Helper helper) {
        return new HelperResponse(helper);
    }
    
    public List<HelperResponse> toHelperResponseList(List<Helper> helpers) {
        return helpers.stream()
                .map(HelperResponse::new)
                .collect(Collectors.toList());
    }
    
    public ServiceRequestResponse toServiceRequestResponse(ServiceRequest serviceRequest) {
        return new ServiceRequestResponse(serviceRequest);
    }
    
    public List<ServiceRequestResponse> toServiceRequestResponseList(List<ServiceRequest> serviceRequests) {
        return serviceRequests.stream()
                .map(ServiceRequestResponse::new)
                .collect(Collectors.toList());
    }
    
    public PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(payment);
    }
    
    public List<PaymentResponse> toPaymentResponseList(List<Payment> payments) {
        return payments.stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }
    
    public ReviewResponse toReviewResponse(Review review) {
        return new ReviewResponse(review);
    }
    
    public List<ReviewResponse> toReviewResponseList(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
    
    public ComplaintResponse toComplaintResponse(Complaint complaint) {
        return new ComplaintResponse(complaint);
    }
    
    public List<ComplaintResponse> toComplaintResponseList(List<Complaint> complaints) {
        return complaints.stream()
                .map(ComplaintResponse::new)
                .collect(Collectors.toList());
    }
}