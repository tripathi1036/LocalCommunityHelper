package com.localhelper.dto.request;

import com.localhelper.entity.Complaint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComplaintRequest {
    
    private Long helperId;
    
    private Long serviceRequestId;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Complaint type is required")
    private Complaint.ComplaintType type;
    
    private Complaint.Priority priority = Complaint.Priority.MEDIUM;
    
    // Constructors
    public ComplaintRequest() {}
    
    public ComplaintRequest(Long helperId, Long serviceRequestId, String subject, String description, 
                          Complaint.ComplaintType type, Complaint.Priority priority) {
        this.helperId = helperId;
        this.serviceRequestId = serviceRequestId;
        this.subject = subject;
        this.description = description;
        this.type = type;
        this.priority = priority;
    }
    
    // Getters and Setters
    public Long getHelperId() { return helperId; }
    public void setHelperId(Long helperId) { this.helperId = helperId; }
    
    public Long getServiceRequestId() { return serviceRequestId; }
    public void setServiceRequestId(Long serviceRequestId) { this.serviceRequestId = serviceRequestId; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Complaint.ComplaintType getType() { return type; }
    public void setType(Complaint.ComplaintType type) { this.type = type; }
    
    public Complaint.Priority getPriority() { return priority; }
    public void setPriority(Complaint.Priority priority) { this.priority = priority; }
}