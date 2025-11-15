package com.localhelper.repository;

import com.localhelper.entity.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface HelperRepository extends JpaRepository<Helper, Long> {
    
    Optional<Helper> findByUserId(Long userId);
    
    List<Helper> findByStatus(Helper.HelperStatus status);
    
    Page<Helper> findByStatus(Helper.HelperStatus status, Pageable pageable);
    
    List<Helper> findByKycStatus(Helper.KycStatus kycStatus);
    
    Page<Helper> findByKycStatus(Helper.KycStatus kycStatus, Pageable pageable);
    
    List<Helper> findByIsAvailable(Boolean isAvailable);
    
    Page<Helper> findByIsAvailable(Boolean isAvailable, Pageable pageable);
    
    List<Helper> findByServiceTypeContainingIgnoreCase(String serviceType);
    
    Page<Helper> findByServiceTypeContainingIgnoreCase(String serviceType, Pageable pageable);
    
    @Query("SELECT h FROM Helper h WHERE h.status = :status AND h.isAvailable = :isAvailable")
    List<Helper> findByStatusAndIsAvailable(@Param("status") Helper.HelperStatus status, @Param("isAvailable") Boolean isAvailable);
    
    @Query("SELECT h FROM Helper h WHERE h.status = :status AND h.isAvailable = :isAvailable")
    Page<Helper> findByStatusAndIsAvailable(@Param("status") Helper.HelperStatus status, @Param("isAvailable") Boolean isAvailable, Pageable pageable);
    
    @Query("SELECT h FROM Helper h WHERE " +
           "h.status = 'APPROVED' AND h.isAvailable = true AND " +
           "(LOWER(h.serviceType) LIKE LOWER(CONCAT('%', :serviceType, '%')) OR " +
           "LOWER(h.description) LIKE LOWER(CONCAT('%', :serviceType, '%')))")
    Page<Helper> searchAvailableHelpers(@Param("serviceType") String serviceType, Pageable pageable);
    
    @Query("SELECT h FROM Helper h WHERE " +
           "h.status = 'APPROVED' AND h.isAvailable = true AND " +
           "h.hourlyRate BETWEEN :minRate AND :maxRate")
    Page<Helper> findByHourlyRateBetween(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate, Pageable pageable);
    
    @Query("SELECT h FROM Helper h WHERE " +
           "h.status = 'APPROVED' AND h.isAvailable = true AND " +
           "h.rating >= :minRating")
    Page<Helper> findByRatingGreaterThanEqual(@Param("minRating") BigDecimal minRating, Pageable pageable);
    
    @Query("SELECT COUNT(h) FROM Helper h WHERE h.status = :status")
    Long countByStatus(@Param("status") Helper.HelperStatus status);
    
    @Query("SELECT COUNT(h) FROM Helper h WHERE h.kycStatus = :kycStatus")
    Long countByKycStatus(@Param("kycStatus") Helper.KycStatus kycStatus);
}