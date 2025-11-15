package com.localhelper.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
    
    @NotNull(message = "Helper ID is required")
    private Long helperId;
    
    @NotNull(message = "Service request ID is required")
    private Long serviceRequestId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    private String comment;
    
    // Constructors
    public ReviewRequest() {}
    
    public ReviewRequest(Long helperId, Long serviceRequestId, Integer rating, String comment) {
        this.helperId = helperId;
        this.serviceRequestId = serviceRequestId;
        this.rating = rating;
        this.comment = comment;
    }
    
    // Getters and Setters
    public Long getHelperId() { return helperId; }
    public void setHelperId(Long helperId) { this.helperId = helperId; }
    
    public Long getServiceRequestId() { return serviceRequestId; }
    public void setServiceRequestId(Long serviceRequestId) { this.serviceRequestId = serviceRequestId; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}