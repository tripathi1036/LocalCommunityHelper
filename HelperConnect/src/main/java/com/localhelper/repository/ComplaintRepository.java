package com.localhelper.repository;

import com.localhelper.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    
    List<Complaint> findByUserId(Long userId);
    
    Page<Complaint> findByUserId(Long userId, Pageable pageable);
    
    List<Complaint> findByHelperId(Long helperId);
    
    Page<Complaint> findByHelperId(Long helperId, Pageable pageable);
    
    List<Complaint> findByServiceRequestId(Long serviceRequestId);
    
    List<Complaint> findByStatus(Complaint.ComplaintStatus status);
    
    Page<Complaint> findByStatus(Complaint.ComplaintStatus status, Pageable pageable);
    
    List<Complaint> findByType(Complaint.ComplaintType type);
    
    Page<Complaint> findByType(Complaint.ComplaintType type, Pageable pageable);
    
    List<Complaint> findByPriority(Complaint.Priority priority);
    
    Page<Complaint> findByPriority(Complaint.Priority priority, Pageable pageable);
    
    @Query("SELECT c FROM Complaint c WHERE c.status = :status AND c.priority = :priority")
    List<Complaint> findByStatusAndPriority(@Param("status") Complaint.ComplaintStatus status, @Param("priority") Complaint.Priority priority);
    
    @Query("SELECT c FROM Complaint c WHERE c.status = :status AND c.priority = :priority")
    Page<Complaint> findByStatusAndPriority(@Param("status") Complaint.ComplaintStatus status, @Param("priority") Complaint.Priority priority, Pageable pageable);
    
    @Query("SELECT c FROM Complaint c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Complaint> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT c FROM Complaint c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    Page<Complaint> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT c FROM Complaint c WHERE " +
           "(LOWER(c.subject) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Complaint> searchComplaints(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.status = :status")
    Long countByStatus(@Param("status") Complaint.ComplaintStatus status);
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.type = :type")
    Long countByType(@Param("type") Complaint.ComplaintType type);
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.priority = :priority")
    Long countByPriority(@Param("priority") Complaint.Priority priority);
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.helper.id = :helperId")
    Long countByHelperId(@Param("helperId") Long helperId);
}