package com.localhelper.service;

import com.localhelper.dto.request.PaymentRequest;
import com.localhelper.dto.response.PaymentResponse;
import com.localhelper.entity.Payment;
import com.localhelper.entity.ServiceRequest;
import com.localhelper.exception.BusinessException;
import com.localhelper.repository.PaymentRepository;
import com.localhelper.repository.ServiceRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    private HelperService helperService;
    
    public PaymentResponse createPayment(PaymentRequest request) {
        logger.info("Creating payment for service request ID: {}", request.getServiceRequestId());
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(request.getServiceRequestId())
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + request.getServiceRequestId()));
        
        if (serviceRequest.getStatus() != ServiceRequest.RequestStatus.COMPLETED) {
            throw new BusinessException("INVALID_OPERATION", "Payment can only be made for completed service requests");
        }
        
        // Check if payment already exists for this service request
        List<Payment> existingPayments = paymentRepository.findByServiceRequestId(request.getServiceRequestId());
        boolean hasSuccessfulPayment = existingPayments.stream()
                .anyMatch(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED);
        
        if (hasSuccessfulPayment) {
            throw new BusinessException("DUPLICATE_PAYMENT", "Payment already completed for this service request");
        }
        
        Payment payment = new Payment();
        payment.setServiceRequest(serviceRequest);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentReference(request.getPaymentReference() != null ? 
                request.getPaymentReference() : generatePaymentReference());
        
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment created successfully with ID: {}", savedPayment.getId());
        
        return new PaymentResponse(savedPayment);
    }
    
    public PaymentResponse processPayment(Long paymentId) {
        logger.info("Processing payment with ID: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("PAYMENT_NOT_FOUND", "Payment not found with ID: " + paymentId));
        
        if (payment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new BusinessException("INVALID_OPERATION", "Only pending payments can be processed");
        }
        
        try {
            payment.setStatus(Payment.PaymentStatus.PROCESSING);
            paymentRepository.save(payment);
            
            // Simulate payment processing (in real implementation, integrate with payment gateway)
            boolean paymentSuccess = simulatePaymentProcessing(payment);
            
            if (paymentSuccess) {
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                payment.setTransactionId(generateTransactionId());
                payment.setProcessedAt(LocalDateTime.now());
                payment.setPaymentGatewayResponse("Payment successful");
                
                // Update helper earnings
                if (payment.getServiceRequest().getHelper() != null) {
                    helperService.updateHelperEarnings(
                            payment.getServiceRequest().getHelper().getId(), 
                            payment.getAmount()
                    );
                }
                
                logger.info("Payment processed successfully with ID: {}", paymentId);
            } else {
                payment.setStatus(Payment.PaymentStatus.FAILED);
                payment.setFailureReason("Payment gateway declined the transaction");
                payment.setPaymentGatewayResponse("Payment failed");
                
                logger.warn("Payment processing failed for ID: {}", paymentId);
            }
            
            Payment updatedPayment = paymentRepository.save(payment);
            return new PaymentResponse(updatedPayment);
            
        } catch (Exception e) {
            logger.error("Error processing payment with ID: {}", paymentId, e);
            
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Internal error during payment processing");
            paymentRepository.save(payment);
            
            throw new BusinessException("PAYMENT_PROCESSING_ERROR", "Failed to process payment: " + e.getMessage());
        }
    }
    
    public PaymentResponse refundPayment(Long paymentId, String reason) {
        logger.info("Refunding payment with ID: {} for reason: {}", paymentId, reason);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("PAYMENT_NOT_FOUND", "Payment not found with ID: " + paymentId));
        
        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new BusinessException("INVALID_OPERATION", "Only completed payments can be refunded");
        }
        
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setFailureReason(reason);
        payment.setPaymentGatewayResponse("Refund processed");
        
        // Reverse helper earnings
        if (payment.getServiceRequest().getHelper() != null) {
            helperService.updateHelperEarnings(
                    payment.getServiceRequest().getHelper().getId(), 
                    payment.getAmount().negate()
            );
        }
        
        Payment refundedPayment = paymentRepository.save(payment);
        logger.info("Payment refunded successfully with ID: {}", paymentId);
        
        return new PaymentResponse(refundedPayment);
    }
    
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PAYMENT_NOT_FOUND", "Payment not found with ID: " + id));
        return new PaymentResponse(payment);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByServiceRequestId(Long serviceRequestId) {
        return paymentRepository.findByServiceRequestId(serviceRequestId).stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByUserId(Long userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable)
                .map(PaymentResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByHelperId(Long helperId, Pageable pageable) {
        return paymentRepository.findByHelperId(helperId, pageable)
                .map(PaymentResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable)
                .map(PaymentResponse::new);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalEarningsByHelperId(Long helperId) {
        BigDecimal earnings = paymentRepository.calculateTotalEarningsByHelperId(helperId);
        return earnings != null ? earnings : BigDecimal.ZERO;
    }
    
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalRevenueBetween(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = paymentRepository.calculateTotalRevenueBetween(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    @Transactional(readOnly = true)
    public Long getTotalPaymentCount() {
        return paymentRepository.count();
    }
    
    @Transactional(readOnly = true)
    public Long getPaymentCountByStatus(Payment.PaymentStatus status) {
        return paymentRepository.countByStatus(status);
    }
    
    // Helper methods
    private String generatePaymentReference() {
        return "PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
    
    private boolean simulatePaymentProcessing(Payment payment) {
        // Simulate payment gateway processing
        // In real implementation, integrate with actual payment gateway (Stripe, PayPal, etc.)
        try {
            Thread.sleep(2000); // Simulate processing time
            return Math.random() > 0.1; // 90% success rate for simulation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}