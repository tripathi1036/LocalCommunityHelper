package com.localhelper.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServiceRequestRequest {
    
    @NotBlank(message = "Service type is required")
    private String serviceType;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;
    
    @DecimalMin(value = "0.0", message = "Duration must be positive")
    private BigDecimal durationHours;
    
    @DecimalMin(value = "0.0", message = "Budget must be positive")
    private BigDecimal budget;
    
    private String notes;
    
    // Constructors
    public ServiceRequestRequest() {}
    
    public ServiceRequestRequest(String serviceType, String description, String location, 
                               LocalDateTime scheduledDate, BigDecimal durationHours, BigDecimal budget, String notes) {
        this.serviceType = serviceType;
        this.description = description;
        this.location = location;
        this.scheduledDate = scheduledDate;
        this.durationHours = durationHours;
        this.budget = budget;
        this.notes = notes;
    }
    
    // Getters and Setters
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
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}