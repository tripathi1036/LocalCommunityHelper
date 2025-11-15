package com.localhelper.dto.response;

import com.localhelper.entity.Helper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HelperResponse {
    
    private Long id;
    private UserResponse user;
    private String serviceType;
    private String description;
    private BigDecimal hourlyRate;
    private String experience;
    private Helper.KycStatus kycStatus;
    private Helper.HelperStatus status;
    private Boolean isAvailable;
    private BigDecimal rating;
    private Integer totalReviews;
    private BigDecimal totalEarnings;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public HelperResponse() {}
    
    public HelperResponse(Helper helper) {
        this.id = helper.getId();
        this.user = new UserResponse(helper.getUser());
        this.serviceType = helper.getServiceType();
        this.description = helper.getDescription();
        this.hourlyRate = helper.getHourlyRate();
        this.experience = helper.getExperience();
        this.kycStatus = helper.getKycStatus();
        this.status = helper.getStatus();
        this.isAvailable = helper.getIsAvailable();
        this.rating = helper.getRating();
        this.totalReviews = helper.getTotalReviews();
        this.totalEarnings = helper.getTotalEarnings();
        this.rejectionReason = helper.getRejectionReason();
        this.createdAt = helper.getCreatedAt();
        this.updatedAt = helper.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
    
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    
    public Helper.KycStatus getKycStatus() { return kycStatus; }
    public void setKycStatus(Helper.KycStatus kycStatus) { this.kycStatus = kycStatus; }
    
    public Helper.HelperStatus getStatus() { return status; }
    public void setStatus(Helper.HelperStatus status) { this.status = status; }
    
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    
    public BigDecimal getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(BigDecimal totalEarnings) { this.totalEarnings = totalEarnings; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}