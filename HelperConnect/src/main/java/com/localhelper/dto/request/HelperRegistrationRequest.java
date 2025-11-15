package com.localhelper.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class HelperRegistrationRequest {
    
    @NotBlank(message = "Service type is required")
    private String serviceType;
    
    private String description;
    
    @NotNull(message = "Hourly rate is required")
    @DecimalMin(value = "0.0", message = "Hourly rate must be positive")
    private BigDecimal hourlyRate;
    
    private String experience;
    
    @NotBlank(message = "KYC document type is required")
    private String kycDocumentType;
    
    @NotBlank(message = "KYC document number is required")
    private String kycDocumentNumber;
    
    @NotBlank(message = "KYC document URL is required")
    private String kycDocumentUrl;
    
    // Constructors
    public HelperRegistrationRequest() {}
    
    public HelperRegistrationRequest(String serviceType, String description, BigDecimal hourlyRate, 
                                   String experience, String kycDocumentType, String kycDocumentNumber, String kycDocumentUrl) {
        this.serviceType = serviceType;
        this.description = description;
        this.hourlyRate = hourlyRate;
        this.experience = experience;
        this.kycDocumentType = kycDocumentType;
        this.kycDocumentNumber = kycDocumentNumber;
        this.kycDocumentUrl = kycDocumentUrl;
    }
    
    // Getters and Setters
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
}