package com.localhelper.service;

import com.localhelper.dto.request.HelperRegistrationRequest;
import com.localhelper.dto.response.HelperResponse;
import com.localhelper.entity.Helper;
import com.localhelper.entity.User;
import com.localhelper.exception.BusinessException;
import com.localhelper.repository.HelperRepository;
import com.localhelper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HelperService {
    
    private static final Logger logger = LoggerFactory.getLogger(HelperService.class);
    
    @Autowired
    private HelperRepository helperRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public HelperResponse registerHelper(Long userId, HelperRegistrationRequest request) {
        logger.info("Registering helper for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        // Check if user is already a helper
        if (helperRepository.findByUserId(userId).isPresent()) {
            throw new BusinessException("DUPLICATE_HELPER", "User is already registered as a helper");
        }
        
        // Create new helper profile
        Helper helper = new Helper();
        helper.setUser(user);
        helper.setServiceType(request.getServiceType());
        helper.setDescription(request.getDescription());
        helper.setHourlyRate(request.getHourlyRate());
        helper.setExperience(request.getExperience());
        helper.setKycDocumentType(request.getKycDocumentType());
        helper.setKycDocumentNumber(request.getKycDocumentNumber());
        helper.setKycDocumentUrl(request.getKycDocumentUrl());
        helper.setKycStatus(Helper.KycStatus.PENDING);
        helper.setStatus(Helper.HelperStatus.PENDING);
        helper.setIsAvailable(false); // Initially not available until approved
        
        // Update user role to HELPER
        user.setRole(User.Role.HELPER);
        userRepository.save(user);
        
        Helper savedHelper = helperRepository.save(helper);
        logger.info("Helper registered successfully with ID: {}", savedHelper.getId());
        
        return new HelperResponse(savedHelper);
    }
    
    @Transactional(readOnly = true)
    public HelperResponse getHelperById(Long id) {
        Helper helper = helperRepository.findById(id)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + id));
        return new HelperResponse(helper);
    }
    
    @Transactional(readOnly = true)
    public HelperResponse getHelperByUserId(Long userId) {
        Helper helper = helperRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found for user ID: " + userId));
        return new HelperResponse(helper);
    }
    
    @Transactional(readOnly = true)
    public List<HelperResponse> getAllHelpers() {
        return helperRepository.findAll().stream()
                .map(HelperResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<HelperResponse> getAllHelpers(Pageable pageable) {
        return helperRepository.findAll(pageable)
                .map(HelperResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<HelperResponse> getHelpersByStatus(Helper.HelperStatus status, Pageable pageable) {
        return helperRepository.findByStatus(status, pageable)
                .map(HelperResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<HelperResponse> getAvailableHelpers(Pageable pageable) {
        return helperRepository.findByStatusAndIsAvailable(Helper.HelperStatus.APPROVED, true, pageable)
                .map(HelperResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<HelperResponse> searchHelpers(String serviceType, Pageable pageable) {
        return helperRepository.searchAvailableHelpers(serviceType, pageable)
                .map(HelperResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<HelperResponse> getHelpersByPriceRange(BigDecimal minRate, BigDecimal maxRate, Pageable pageable) {
        return helperRepository.findByHourlyRateBetween(minRate, maxRate, pageable)
                .map(HelperResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<HelperResponse> getHelpersByRating(BigDecimal minRating, Pageable pageable) {
        return helperRepository.findByRatingGreaterThanEqual(minRating, pageable)
                .map(HelperResponse::new);
    }
    
    public HelperResponse updateHelperProfile(Long helperId, HelperRegistrationRequest request) {
        logger.info("Updating helper profile with ID: {}", helperId);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        helper.setServiceType(request.getServiceType());
        helper.setDescription(request.getDescription());
        helper.setHourlyRate(request.getHourlyRate());
        helper.setExperience(request.getExperience());
        
        Helper updatedHelper = helperRepository.save(helper);
        logger.info("Helper profile updated successfully with ID: {}", updatedHelper.getId());
        
        return new HelperResponse(updatedHelper);
    }
    
    public HelperResponse updateAvailability(Long helperId, Boolean isAvailable) {
        logger.info("Updating availability for helper ID: {} to {}", helperId, isAvailable);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        if (helper.getStatus() != Helper.HelperStatus.APPROVED) {
            throw new BusinessException("INVALID_OPERATION", "Only approved helpers can update availability");
        }
        
        helper.setIsAvailable(isAvailable);
        Helper updatedHelper = helperRepository.save(helper);
        
        logger.info("Availability updated successfully for helper ID: {}", helperId);
        return new HelperResponse(updatedHelper);
    }
    
    public HelperResponse approveHelper(Long helperId) {
        logger.info("Approving helper with ID: {}", helperId);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        if (helper.getKycStatus() != Helper.KycStatus.APPROVED) {
            throw new BusinessException("INVALID_OPERATION", "KYC must be approved before helper approval");
        }
        
        helper.setStatus(Helper.HelperStatus.APPROVED);
        helper.setIsAvailable(true);
        helper.setRejectionReason(null);
        
        Helper approvedHelper = helperRepository.save(helper);
        logger.info("Helper approved successfully with ID: {}", helperId);
        
        return new HelperResponse(approvedHelper);
    }
    
    public HelperResponse rejectHelper(Long helperId, String reason) {
        logger.info("Rejecting helper with ID: {} for reason: {}", helperId, reason);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        helper.setStatus(Helper.HelperStatus.REJECTED);
        helper.setIsAvailable(false);
        helper.setRejectionReason(reason);
        
        Helper rejectedHelper = helperRepository.save(helper);
        logger.info("Helper rejected successfully with ID: {}", helperId);
        
        return new HelperResponse(rejectedHelper);
    }
    
    public HelperResponse approveKyc(Long helperId) {
        logger.info("Approving KYC for helper ID: {}", helperId);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        helper.setKycStatus(Helper.KycStatus.APPROVED);
        Helper updatedHelper = helperRepository.save(helper);
        
        logger.info("KYC approved successfully for helper ID: {}", helperId);
        return new HelperResponse(updatedHelper);
    }
    
    public HelperResponse rejectKyc(Long helperId, String reason) {
        logger.info("Rejecting KYC for helper ID: {} for reason: {}", helperId, reason);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        helper.setKycStatus(Helper.KycStatus.REJECTED);
        helper.setStatus(Helper.HelperStatus.REJECTED);
        helper.setIsAvailable(false);
        helper.setRejectionReason(reason);
        
        Helper rejectedHelper = helperRepository.save(helper);
        logger.info("KYC rejected successfully for helper ID: {}", helperId);
        
        return new HelperResponse(rejectedHelper);
    }
    
    public void updateHelperRating(Long helperId, BigDecimal newRating, Integer totalReviews) {
        logger.info("Updating rating for helper ID: {} to {}", helperId, newRating);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        helper.setRating(newRating);
        helper.setTotalReviews(totalReviews);
        helperRepository.save(helper);
        
        logger.info("Rating updated successfully for helper ID: {}", helperId);
    }
    
    public void updateHelperEarnings(Long helperId, BigDecimal additionalEarnings) {
        logger.info("Updating earnings for helper ID: {} with additional: {}", helperId, additionalEarnings);
        
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + helperId));
        
        BigDecimal currentEarnings = helper.getTotalEarnings() != null ? helper.getTotalEarnings() : BigDecimal.ZERO;
        helper.setTotalEarnings(currentEarnings.add(additionalEarnings));
        
        helperRepository.save(helper);
        logger.info("Earnings updated successfully for helper ID: {}", helperId);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalHelperCount() {
        return helperRepository.count();
    }
    
    @Transactional(readOnly = true)
    public Long getHelperCountByStatus(Helper.HelperStatus status) {
        return helperRepository.countByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public Long getHelperCountByKycStatus(Helper.KycStatus kycStatus) {
        return helperRepository.countByKycStatus(kycStatus);
    }
}