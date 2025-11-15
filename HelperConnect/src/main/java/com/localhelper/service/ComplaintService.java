package com.localhelper.service;

import com.localhelper.dto.request.ComplaintRequest;
import com.localhelper.dto.response.ComplaintResponse;
import com.localhelper.entity.Complaint;
import com.localhelper.entity.Helper;
import com.localhelper.entity.ServiceRequest;
import com.localhelper.entity.User;
import com.localhelper.exception.BusinessException;
import com.localhelper.repository.ComplaintRepository;
import com.localhelper.repository.HelperRepository;
import com.localhelper.repository.ServiceRequestRepository;
import com.localhelper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComplaintService {
    
    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);
    
    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HelperRepository helperRepository;
    
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    public ComplaintResponse createComplaint(Long userId, ComplaintRequest request) {
        logger.info("Creating complaint by user ID: {} with subject: {}", userId, request.getSubject());
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        Complaint complaint = new Complaint();
        complaint.setUser(user);
        complaint.setSubject(request.getSubject());
        complaint.setDescription(request.getDescription());
        complaint.setType(request.getType());
        complaint.setPriority(request.getPriority());
        complaint.setStatus(Complaint.ComplaintStatus.OPEN);
        
        // Set helper if provided
        if (request.getHelperId() != null) {
            Helper helper = helperRepository.findById(request.getHelperId())
                    .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + request.getHelperId()));
            complaint.setHelper(helper);
        }
        
        // Set service request if provided
        if (request.getServiceRequestId() != null) {
            ServiceRequest serviceRequest = serviceRequestRepository.findById(request.getServiceRequestId())
                    .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + request.getServiceRequestId()));
            
            // Validate that the service request belongs to the user
            if (!serviceRequest.getUser().getId().equals(userId)) {
                throw new BusinessException("UNAUTHORIZED_ACCESS", "User can only file complaints for their own service requests");
            }
            
            complaint.setServiceRequest(serviceRequest);
            
            // If service request is provided, set helper automatically
            if (serviceRequest.getHelper() != null) {
                complaint.setHelper(serviceRequest.getHelper());
            }
        }
        
        Complaint savedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint created successfully with ID: {}", savedComplaint.getId());
        
        return new ComplaintResponse(savedComplaint);
    }
    
    @Transactional(readOnly = true)
    public ComplaintResponse getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new BusinessException("COMPLAINT_NOT_FOUND", "Complaint not found with ID: " + id));
        return new ComplaintResponse(complaint);
    }
    
    @Transactional(readOnly = true)
    public List<ComplaintResponse> getComplaintsByUserId(Long userId) {
        return complaintRepository.findByUserId(userId).stream()
                .map(ComplaintResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getComplaintsByUserId(Long userId, Pageable pageable) {
        return complaintRepository.findByUserId(userId, pageable)
                .map(ComplaintResponse::new);
    }
    
    @Transactional(readOnly = true)
    public List<ComplaintResponse> getComplaintsByHelperId(Long helperId) {
        return complaintRepository.findByHelperId(helperId).stream()
                .map(ComplaintResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getComplaintsByHelperId(Long helperId, Pageable pageable) {
        return complaintRepository.findByHelperId(helperId, pageable)
                .map(ComplaintResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getComplaintsByStatus(Complaint.ComplaintStatus status, Pageable pageable) {
        return complaintRepository.findByStatus(status, pageable)
                .map(ComplaintResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getComplaintsByType(Complaint.ComplaintType type, Pageable pageable) {
        return complaintRepository.findByType(type, pageable)
                .map(ComplaintResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getComplaintsByPriority(Complaint.Priority priority, Pageable pageable) {
        return complaintRepository.findByPriority(priority, pageable)
                .map(ComplaintResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> searchComplaints(String searchTerm, Pageable pageable) {
        return complaintRepository.searchComplaints(searchTerm, pageable)
                .map(ComplaintResponse::new);
    }
    
    public ComplaintResponse updateComplaintStatus(Long complaintId, Complaint.ComplaintStatus status) {
        logger.info("Updating complaint ID: {} status to: {}", complaintId, status);
        
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException("COMPLAINT_NOT_FOUND", "Complaint not found with ID: " + complaintId));
        
        complaint.setStatus(status);
        
        if (status == Complaint.ComplaintStatus.RESOLVED || status == Complaint.ComplaintStatus.CLOSED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }
        
        Complaint updatedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint status updated successfully for ID: {}", complaintId);
        
        return new ComplaintResponse(updatedComplaint);
    }
    
    public ComplaintResponse updateComplaintPriority(Long complaintId, Complaint.Priority priority) {
        logger.info("Updating complaint ID: {} priority to: {}", complaintId, priority);
        
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException("COMPLAINT_NOT_FOUND", "Complaint not found with ID: " + complaintId));
        
        complaint.setPriority(priority);
        
        Complaint updatedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint priority updated successfully for ID: {}", complaintId);
        
        return new ComplaintResponse(updatedComplaint);
    }
    
    public ComplaintResponse resolveComplaint(Long complaintId, Long adminUserId, String response) {
        logger.info("Resolving complaint ID: {} by admin user ID: {}", complaintId, adminUserId);
        
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException("COMPLAINT_NOT_FOUND", "Complaint not found with ID: " + complaintId));
        
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "Admin user not found with ID: " + adminUserId));
        
        if (adminUser.getRole() != User.Role.ADMIN) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "Only admin users can resolve complaints");
        }
        
        complaint.setStatus(Complaint.ComplaintStatus.RESOLVED);
        complaint.setAdminResponse(response);
        complaint.setResolvedAt(LocalDateTime.now());
        complaint.setResolvedBy(adminUser);
        
        Complaint resolvedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint resolved successfully with ID: {}", complaintId);
        
        return new ComplaintResponse(resolvedComplaint);
    }
    
    public ComplaintResponse closeComplaint(Long complaintId, Long adminUserId, String response) {
        logger.info("Closing complaint ID: {} by admin user ID: {}", complaintId, adminUserId);
        
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException("COMPLAINT_NOT_FOUND", "Complaint not found with ID: " + complaintId));
        
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "Admin user not found with ID: " + adminUserId));
        
        if (adminUser.getRole() != User.Role.ADMIN) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "Only admin users can close complaints");
        }
        
        complaint.setStatus(Complaint.ComplaintStatus.CLOSED);
        complaint.setAdminResponse(response);
        complaint.setResolvedAt(LocalDateTime.now());
        complaint.setResolvedBy(adminUser);
        
        Complaint closedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint closed successfully with ID: {}", complaintId);
        
        return new ComplaintResponse(closedComplaint);
    }
    
    public ComplaintResponse reopenComplaint(Long complaintId) {
        logger.info("Reopening complaint ID: {}", complaintId);
        
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException("COMPLAINT_NOT_FOUND", "Complaint not found with ID: " + complaintId));
        
        if (complaint.getStatus() != Complaint.ComplaintStatus.CLOSED && 
            complaint.getStatus() != Complaint.ComplaintStatus.RESOLVED) {
            throw new BusinessException("INVALID_OPERATION", "Only closed or resolved complaints can be reopened");
        }
        
        complaint.setStatus(Complaint.ComplaintStatus.OPEN);
        complaint.setResolvedAt(null);
        complaint.setResolvedBy(null);
        
        Complaint reopenedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint reopened successfully with ID: {}", complaintId);
        
        return new ComplaintResponse(reopenedComplaint);
    }
    
    public void deleteComplaint(Long complaintId, Long userId) {
        logger.info("Deleting complaint ID: {} by user ID: {}", complaintId, userId);
        
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException("COMPLAINT_NOT_FOUND", "Complaint not found with ID: " + complaintId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        // Only the complaint creator or admin can delete
        if (!complaint.getUser().getId().equals(userId) && user.getRole() != User.Role.ADMIN) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "Only the complaint creator or admin can delete complaints");
        }
        
        complaintRepository.delete(complaint);
        logger.info("Complaint deleted successfully with ID: {}", complaintId);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalComplaintCount() {
        return complaintRepository.count();
    }
    
    @Transactional(readOnly = true)
    public Long getComplaintCountByStatus(Complaint.ComplaintStatus status) {
        return complaintRepository.countByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public Long getComplaintCountByType(Complaint.ComplaintType type) {
        return complaintRepository.countByType(type);
    }
    
    @Transactional(readOnly = true)
    public Long getComplaintCountByPriority(Complaint.Priority priority) {
        return complaintRepository.countByPriority(priority);
    }
    
    @Transactional(readOnly = true)
    public Long getComplaintCountByHelperId(Long helperId) {
        return complaintRepository.countByHelperId(helperId);
    }
}