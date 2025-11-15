package com.localhelper.dto.response;

import com.localhelper.entity.Review;

import java.time.LocalDateTime;

public class ReviewResponse {
    
    private Long id;
    private UserResponse user;
    private Long helperId;
    private String helperName;
    private Long serviceRequestId;
    private Integer rating;
    private String comment;
    private Boolean isVisible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ReviewResponse() {}
    
    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.user = new UserResponse(review.getUser());
        this.helperId = review.getHelper().getId();
        this.helperName = review.getHelper().getUser().getFullName();
        this.serviceRequestId = review.getServiceRequest().getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.isVisible = review.getIsVisible();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
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
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}