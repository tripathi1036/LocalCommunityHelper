package com.localhelper.service;

import com.localhelper.dto.request.ServiceRequestRequest;
import com.localhelper.dto.response.ServiceRequestResponse;
import com.localhelper.entity.Helper;
import com.localhelper.entity.ServiceRequest;
import com.localhelper.entity.User;
import com.localhelper.exception.BusinessException;
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
public class ServiceRequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestService.class);
    
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HelperRepository helperRepository;
    
    public ServiceRequestResponse createServiceRequest(Long userId, ServiceRequestRequest request) {
        logger.info("Creating service request for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setUser(user);
        serviceRequest.setServiceType(request.getServiceType());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setLocation(request.getLocation());
        serviceRequest.setScheduledDate(request.getScheduledDate());
        serviceRequest.setDurationHours(request.getDurationHours());
        serviceRequest.setBudget(request.getBudget());
        serviceRequest.setNotes(request.getNotes());
        serviceRequest.setStatus(ServiceRequest.RequestStatus.PENDING);
        
        ServiceRequest savedRequest = serviceRequestRepository.save(serviceRequest);
        logger.info("Service request created successfully with ID: {}", savedRequest.getId());
        
        return new ServiceRequestResponse(savedRequest);
    }
    
    @Transactional(readOnly = true)
    public ServiceRequestResponse getServiceRequestById(Long id) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + id));
        return new ServiceRequestResponse(serviceRequest);
    }
    
    @Transactional(readOnly = true)
    public List<ServiceRequestResponse> getServiceRequestsByUserId(Long userId) {
        return serviceRequestRepository.findByUserId(userId).stream()
                .map(ServiceRequestResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceRequestResponse> getServiceRequestsByUserId(Long userId, Pageable pageable) {
        return serviceRequestRepository.findByUserId(userId, pageable)
                .map(ServiceRequestResponse::new);
    }
    
    @Transactional(readOnly = true)
    public List<ServiceRequestResponse> getServiceRequestsByHelperId(Long helperId) {
        return serviceRequestRepository.findByHelperId(helperId).stream()
                .map(ServiceRequestResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceRequestResponse> getServiceRequestsByHelperId(Long helperId, Pageable pageable) {
        return serviceRequestRepository.findByHelperId(helperId, pageable)
                .map(ServiceRequestResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceRequestResponse> getServiceRequestsByStatus(ServiceRequest.RequestStatus status, Pageable pageable) {
        return serviceRequestRepository.findByStatus(status, pageable)
                .map(ServiceRequestResponse::new);
    }
    
    @Transactional(readOnly = true)
    public List<ServiceRequestResponse> getPendingRequestsByServiceType(String serviceType) {
        return serviceRequestRepository.findPendingRequestsByServiceType(serviceType).stream()
                .map(ServiceRequestResponse::new)
                .collect(Collectors.toList());
    }
    
    public ServiceRequestResponse acceptServiceRequest(Long requestId, Long helperId) {
        logger.info("Helper ID: {} accepting service request ID: {}", helperId, requestId);
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + requestId));
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        if (serviceRequest.getStatus() != ServiceRequest.RequestStatus.PENDING) {
            throw new BusinessException("INVALID_OPERATION", "Service request is not in pending status");
        }
        
        if (helper.getStatus() != Helper.HelperStatus.APPROVED || !helper.getIsAvailable()) {
            throw new BusinessException("INVALID_OPERATION", "Helper is not approved or available");
        }
        
        serviceRequest.setHelper(helper);
        serviceRequest.setStatus(ServiceRequest.RequestStatus.ACCEPTED);
        
        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        logger.info("Service request accepted successfully with ID: {}", requestId);
        
        return new ServiceRequestResponse(updatedRequest);
    }
    
    public ServiceRequestResponse rejectServiceRequest(Long requestId, Long helperId, String reason) {
        logger.info("Helper ID: {} rejecting service request ID: {} for reason: {}", helperId, requestId, reason);
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + requestId));
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        if (serviceRequest.getStatus() != ServiceRequest.RequestStatus.PENDING) {
            throw new BusinessException("INVALID_OPERATION", "Service request is not in pending status");
        }
        
        serviceRequest.setStatus(ServiceRequest.RequestStatus.REJECTED);
        serviceRequest.setRejectionReason(reason);
        
        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        logger.info("Service request rejected successfully with ID: {}", requestId);
        
        return new ServiceRequestResponse(updatedRequest);
    }
    
    public ServiceRequestResponse startService(Long requestId, Long helperId) {
        logger.info("Starting service for request ID: {} by helper ID: {}", requestId, helperId);
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + requestId));
        
        if (!serviceRequest.getHelper().getId().equals(helperId)) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "Only assigned helper can start the service");
        }
        
        if (serviceRequest.getStatus() != ServiceRequest.RequestStatus.ACCEPTED) {
            throw new BusinessException("INVALID_OPERATION", "Service request must be accepted before starting");
        }
        
        serviceRequest.setStatus(ServiceRequest.RequestStatus.IN_PROGRESS);
        
        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        logger.info("Service started successfully for request ID: {}", requestId);
        
        return new ServiceRequestResponse(updatedRequest);
    }
    
    public ServiceRequestResponse completeService(Long requestId, Long helperId) {
        logger.info("Completing service for request ID: {} by helper ID: {}", requestId, helperId);
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + requestId));
        
        if (!serviceRequest.getHelper().getId().equals(helperId)) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "Only assigned helper can complete the service");
        }
        
        if (serviceRequest.getStatus() != ServiceRequest.RequestStatus.IN_PROGRESS) {
            throw new BusinessException("INVALID_OPERATION", "Service request must be in progress before completion");
        }
        
        serviceRequest.setStatus(ServiceRequest.RequestStatus.COMPLETED);
        serviceRequest.setIsCompleted(true);
        serviceRequest.setCompletedAt(LocalDateTime.now());
        
        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        logger.info("Service completed successfully for request ID: {}", requestId);
        
        return new ServiceRequestResponse(updatedRequest);
    }
    
    public ServiceRequestResponse cancelServiceRequest(Long requestId, Long userId) {
        logger.info("Cancelling service request ID: {} by user ID: {}", requestId, userId);
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + requestId));
        
        if (!serviceRequest.getUser().getId().equals(userId)) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "Only the requesting user can cancel the service");
        }
        
        if (serviceRequest.getStatus() == ServiceRequest.RequestStatus.COMPLETED) {
            throw new BusinessException("INVALID_OPERATION", "Cannot cancel completed service request");
        }
        
        serviceRequest.setStatus(ServiceRequest.RequestStatus.CANCELLED);
        
        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        logger.info("Service request cancelled successfully with ID: {}", requestId);
        
        return new ServiceRequestResponse(updatedRequest);
    }
    
    public ServiceRequestResponse updateServiceRequest(Long requestId, Long userId, ServiceRequestRequest request) {
        logger.info("Updating service request ID: {} by user ID: {}", requestId, userId);
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + requestId));
        
        if (!serviceRequest.getUser().getId().equals(userId)) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "Only the requesting user can update the service");
        }
        
        if (serviceRequest.getStatus() != ServiceRequest.RequestStatus.PENDING) {
            throw new BusinessException("INVALID_OPERATION", "Only pending service requests can be updated");
        }
        
        serviceRequest.setServiceType(request.getServiceType());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setLocation(request.getLocation());
        serviceRequest.setScheduledDate(request.getScheduledDate());
        serviceRequest.setDurationHours(request.getDurationHours());
        serviceRequest.setBudget(request.getBudget());
        serviceRequest.setNotes(request.getNotes());
        
        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        logger.info("Service request updated successfully with ID: {}", requestId);
        
        return new ServiceRequestResponse(updatedRequest);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalServiceRequestCount() {
        return serviceRequestRepository.count();
    }
    
    @Transactional(readOnly = true)
    public Long getServiceRequestCountByStatus(ServiceRequest.RequestStatus status) {
        return serviceRequestRepository.countByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public Long getServiceRequestCountByUserId(Long userId) {
        return serviceRequestRepository.countByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public Long getServiceRequestCountByHelperId(Long helperId) {
        return serviceRequestRepository.countByHelperId(helperId);
    }
}