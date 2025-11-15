package com.localhelper.dto.response;

import com.localhelper.entity.ServiceRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServiceRequestResponse {
    
    private Long id;
    private UserResponse user;
    private HelperResponse helper;
    private String serviceType;
    private String description;
    private String location;
    private LocalDateTime scheduledDate;
    private BigDecimal durationHours;
    private BigDecimal budget;
    private ServiceRequest.RequestStatus status;
    private String notes;
    private String rejectionReason;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ServiceRequestResponse() {}
    
    public ServiceRequestResponse(ServiceRequest serviceRequest) {
        this.id = serviceRequest.getId();
        this.user = new UserResponse(serviceRequest.getUser());
        this.helper = serviceRequest.getHelper() != null ? new HelperResponse(serviceRequest.getHelper()) : null;
        this.serviceType = serviceRequest.getServiceType();
        this.description = serviceRequest.getDescription();
        this.location = serviceRequest.getLocation();
        this.scheduledDate = serviceRequest.getScheduledDate();
        this.durationHours = serviceRequest.getDurationHours();
        this.budget = serviceRequest.getBudget();
        this.status = serviceRequest.getStatus();
        this.notes = serviceRequest.getNotes();
        this.rejectionReason = serviceRequest.getRejectionReason();
        this.isCompleted = serviceRequest.getIsCompleted();
        this.completedAt = serviceRequest.getCompletedAt();
        this.createdAt = serviceRequest.getCreatedAt();
        this.updatedAt = serviceRequest.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
    
    public HelperResponse getHelper() { return helper; }
    public void setHelper(HelperResponse helper) { this.helper = helper; }
    
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    
    public BigDecimal getDurationHours() { return durationHours; }
    public void setDurationHours(BigDecimal durationHours) { this.durationHours = durationHours; }
    
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    
    public ServiceRequest.RequestStatus getStatus() { return status; }
    public void setStatus(ServiceRequest.RequestStatus status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}