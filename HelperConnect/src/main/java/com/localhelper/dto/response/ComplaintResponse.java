package com.localhelper.dto.response;

import com.localhelper.entity.Complaint;

import java.time.LocalDateTime;

public class ComplaintResponse {
    
    private Long id;
    private UserResponse user;
    private Long helperId;
    private String helperName;
    private Long serviceRequestId;
    private String subject;
    private String description;
    private Complaint.ComplaintType type;
    private Complaint.ComplaintStatus status;
    private Complaint.Priority priority;
    private String adminResponse;
    private LocalDateTime resolvedAt;
    private String resolvedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ComplaintResponse() {}
    
    public ComplaintResponse(Complaint complaint) {
        this.id = complaint.getId();
        this.user = new UserResponse(complaint.getUser());
        this.helperId = complaint.getHelper() != null ? complaint.getHelper().getId() : null;
        this.helperName = complaint.getHelper() != null ? complaint.getHelper().getUser().getFullName() : null;
        this.serviceRequestId = complaint.getServiceRequest() != null ? complaint.getServiceRequest().getId() : null;
        this.subject = complaint.getSubject();
        this.description = complaint.getDescription();
        this.type = complaint.getType();
        this.status = complaint.getStatus();
        this.priority = complaint.getPriority();
        this.adminResponse = complaint.getAdminResponse();
        this.resolvedAt = complaint.getResolvedAt();
        this.resolvedByName = complaint.getResolvedBy() != null ? complaint.getResolvedBy().getFullName() : null;
        this.createdAt = complaint.getCreatedAt();
        this.updatedAt = complaint.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
    
    public Long getHelperId() { return helperId; }
    public void setHelperId(Long helperId) { this.helperId = helperId; }
    
    public String getHelperName() { return helperName; }
    public void setHelperName(String helperName) { this.helperName = helperName; }
    
    public Long getServiceRequestId() { return serviceRequestId; }
    public void setServiceRequestId(Long serviceRequestId) { this.serviceRequestId = serviceRequestId; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Complaint.ComplaintType getType() { return type; }
    public void setType(Complaint.ComplaintType type) { this.type = type; }
    
    public Complaint.ComplaintStatus getStatus() { return status; }
    public void setStatus(Complaint.ComplaintStatus status) { this.status = status; }
    
    public Complaint.Priority getPriority() { return priority; }
    public void setPriority(Complaint.Priority priority) { this.priority = priority; }
    
    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }
    
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    
    public String getResolvedByName() { return resolvedByName; }
    public void setResolvedByName(String resolvedByName) { this.resolvedByName = resolvedByName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}