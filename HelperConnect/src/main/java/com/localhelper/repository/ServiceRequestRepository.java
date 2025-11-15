package com.localhelper.repository;

import com.localhelper.entity.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    
    List<ServiceRequest> findByUserId(Long userId);
    
    Page<ServiceRequest> findByUserId(Long userId, Pageable pageable);
    
    List<ServiceRequest> findByHelperId(Long helperId);
    
    Page<ServiceRequest> findByHelperId(Long helperId, Pageable pageable);
    
    List<ServiceRequest> findByStatus(ServiceRequest.RequestStatus status);
    
    Page<ServiceRequest> findByStatus(ServiceRequest.RequestStatus status, Pageable pageable);
    
    List<ServiceRequest> findByServiceTypeContainingIgnoreCase(String serviceType);
    
    Page<ServiceRequest> findByServiceTypeContainingIgnoreCase(String serviceType, Pageable pageable);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.user.id = :userId AND sr.status = :status")
    List<ServiceRequest> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ServiceRequest.RequestStatus status);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.helper.id = :helperId AND sr.status = :status")
    List<ServiceRequest> findByHelperIdAndStatus(@Param("helperId") Long helperId, @Param("status") ServiceRequest.RequestStatus status);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.helper.id = :helperId AND sr.status = :status")
    Page<ServiceRequest> findByHelperIdAndStatus(@Param("helperId") Long helperId, @Param("status") ServiceRequest.RequestStatus status, Pageable pageable);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.status = 'PENDING' AND sr.serviceType = :serviceType")
    List<ServiceRequest> findPendingRequestsByServiceType(@Param("serviceType") String serviceType);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.scheduledDate BETWEEN :startDate AND :endDate")
    List<ServiceRequest> findByScheduledDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.scheduledDate BETWEEN :startDate AND :endDate")
    Page<ServiceRequest> findByScheduledDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.isCompleted = :isCompleted")
    List<ServiceRequest> findByIsCompleted(@Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.isCompleted = :isCompleted")
    Page<ServiceRequest> findByIsCompleted(@Param("isCompleted") Boolean isCompleted, Pageable pageable);
    
    @Query("SELECT COUNT(sr) FROM ServiceRequest sr WHERE sr.status = :status")
    Long countByStatus(@Param("status") ServiceRequest.RequestStatus status);
    
    @Query("SELECT COUNT(sr) FROM ServiceRequest sr WHERE sr.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(sr) FROM ServiceRequest sr WHERE sr.helper.id = :helperId")
    Long countByHelperId(@Param("helperId") Long helperId);
}