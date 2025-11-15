package com.localhelper.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "helpers")
public class Helper {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(nullable = false)
    @NotBlank(message = "Service type is required")
    private String serviceType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Hourly rate is required")
    @DecimalMin(value = "0.0", message = "Hourly rate must be positive")
    private BigDecimal hourlyRate;
    
    private String experience;
    
    @Column(nullable = false)
    private String kycDocumentType;
    
    @Column(nullable = false)
    private String kycDocumentNumber;
    
    @Column(nullable = false)
    private String kycDocumentUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus kycStatus = KycStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelperStatus status = HelperStatus.PENDING;
    
    @Column(nullable = false)
    private Boolean isAvailable = true;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private Integer totalReviews = 0;
    
    @Column(precision = 12, scale = 2)
    private BigDecimal totalEarnings = BigDecimal.ZERO;
    
    private String rejectionReason;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "helper", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceRequest> serviceRequests;
    
    @OneToMany(mappedBy = "helper", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
    
    // Enums
    public enum KycStatus {
        PENDING, APPROVED, REJECTED
    }
    
    public enum HelperStatus {
        PENDING, APPROVED, REJECTED, SUSPENDED
    }
    
    // Constructors
    public Helper() {}
    
    public Helper(User user, String serviceType, BigDecimal hourlyRate, String kycDocumentType, String kycDocumentNumber, String kycDocumentUrl) {
        this.user = user;
        this.serviceType = serviceType;
        this.hourlyRate = hourlyRate;
        this.kycDocumentType = kycDocumentType;
        this.kycDocumentNumber = kycDocumentNumber;
        this.kycDocumentUrl = kycDocumentUrl;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    
    public String getKycDocumentType() { return kycDocumentType; }
    public void setKycDocumentType(String kycDocumentType) { this.kycDocumentType = kycDocumentType; }
    
    public String getKycDocumentNumber() { return kycDocumentNumber; }
    public void setKycDocumentNumber(String kycDocumentNumber) { this.kycDocumentNumber = kycDocumentNumber; }
    
    public String getKycDocumentUrl() { return kycDocumentUrl; }
    public void setKycDocumentUrl(String kycDocumentUrl) { this.kycDocumentUrl = kycDocumentUrl; }
    
    public KycStatus getKycStatus() { return kycStatus; }
    public void setKycStatus(KycStatus kycStatus) { this.kycStatus = kycStatus; }
    
    public HelperStatus getStatus() { return status; }
    public void setStatus(HelperStatus status) { this.status = status; }
    
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
    
    public List<ServiceRequest> getServiceRequests() { return serviceRequests; }
    public void setServiceRequests(List<ServiceRequest> serviceRequests) { this.serviceRequests = serviceRequests; }
    
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
}