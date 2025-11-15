package com.localhelper.repository;

import com.localhelper.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByServiceRequestId(Long serviceRequestId);
    
    Page<Payment> findByServiceRequestId(Long serviceRequestId, Pageable pageable);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    Page<Payment> findByStatus(Payment.PaymentStatus status, Pageable pageable);
    
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    Page<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod, Pageable pageable);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByPaymentReference(String paymentReference);
    
    @Query("SELECT p FROM Payment p WHERE p.serviceRequest.user.id = :userId")
    List<Payment> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Payment p WHERE p.serviceRequest.user.id = :userId")
    Page<Payment> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.serviceRequest.helper.id = :helperId")
    List<Payment> findByHelperId(@Param("helperId") Long helperId);
    
    @Query("SELECT p FROM Payment p WHERE p.serviceRequest.helper.id = :helperId")
    Page<Payment> findByHelperId(@Param("helperId") Long helperId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.serviceRequest.helper.id = :helperId AND p.status = 'COMPLETED'")
    List<Payment> findCompletedPaymentsByHelperId(@Param("helperId") Long helperId);
    
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Page<Payment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.serviceRequest.helper.id = :helperId AND p.status = 'COMPLETED'")
    BigDecimal calculateTotalEarningsByHelperId(@Param("helperId") Long helperId);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED' AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") Payment.PaymentStatus status);
}