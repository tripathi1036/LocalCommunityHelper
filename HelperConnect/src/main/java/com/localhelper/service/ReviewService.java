package com.localhelper.service;

import com.localhelper.dto.request.ReviewRequest;
import com.localhelper.dto.response.ReviewResponse;
import com.localhelper.entity.Helper;
import com.localhelper.entity.Review;
import com.localhelper.entity.ServiceRequest;
import com.localhelper.entity.User;
import com.localhelper.exception.BusinessException;
import com.localhelper.repository.HelperRepository;
import com.localhelper.repository.ReviewRepository;
import com.localhelper.repository.ServiceRequestRepository;
import com.localhelper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HelperRepository helperRepository;
    
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    private HelperService helperService;
    
    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        logger.info("Creating review for helper ID: {} by user ID: {}", request.getHelperId(), userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found with ID: " + userId));
        
        Helper helper = helperRepository.findById(request.getHelperId())
                .orElseThrow(() -> new BusinessException("HELPER_NOT_FOUND", "Helper not found with ID: " + request.getHelperId()));
        
        ServiceRequest serviceRequest = serviceRequestRepository.findById(request.getServiceRequestId())
                .orElseThrow(() -> new BusinessException("SERVICE_REQUEST_NOT_FOUND", "Service request not found with ID: " + request.getServiceRequestId()));
        
        // Validate that the service request belongs to the user and helper
        if (!serviceRequest.getUser().getId().equals(userId)) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "User can only review their own service requests");
        }
        
        if (!serviceRequest.getHelper().getId().equals(request.getHelperId())) {
            throw new BusinessException("INVALID_OPERATION", "Helper ID does not match the service request");
        }
        
        if (serviceRequest.getStatus() != ServiceRequest.RequestStatus.COMPLETED) {
            throw new BusinessException("INVALID_OPERATION", "Reviews can only be submitted for completed service requests");
        }
        
        // Check if review already exists for this service request
        List<Review> existingReviews = reviewRepository.findByServiceRequestId(request.getServiceRequestId());
        if (!existingReviews.isEmpty()) {
            throw new BusinessException("DUPLICATE_REVIEW", "Review already exists for this service request");
        }
        
        Review review = new Review();
        review.setUser(user);
        review.setHelper(helper);
        review.setServiceRequest(serviceRequest);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setIsVisible(true);
        
        Review savedReview = reviewRepository.save(review);
        
        // Update helper's rating
        updateHelperRating(request.getHelperId());
        
        logger.info("Review created successfully with ID: {}", savedReview.getId());
        return new ReviewResponse(savedReview);
    }
    
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("REVIEW_NOT_FOUND", "Review not found with ID: " + id));
        return new ReviewResponse(review);
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(ReviewResponse::new);
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByHelperId(Long helperId) {
        return reviewRepository.findByHelperIdAndIsVisible(helperId, true).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByHelperId(Long helperId, Pageable pageable) {
        return reviewRepository.findByHelperIdAndIsVisible(helperId, true, pageable)
                .map(ReviewResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByRating(Integer rating, Pageable pageable) {
        return reviewRepository.findByRating(rating, pageable)
                .map(ReviewResponse::new);
    }
    
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsWithComments(Pageable pageable) {
        return reviewRepository.findReviewsWithComments(pageable)
                .map(ReviewResponse::new);
    }
    
    public ReviewResponse updateReview(Long reviewId, Long userId, ReviewRequest request) {
        logger.info("Updating review ID: {} by user ID: {}", reviewId, userId);
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("REVIEW_NOT_FOUND", "Review not found with ID: " + reviewId));
        
        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "User can only update their own reviews");
        }
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review updatedReview = reviewRepository.save(review);
        
        // Update helper's rating
        updateHelperRating(review.getHelper().getId());
        
        logger.info("Review updated successfully with ID: {}", reviewId);
        return new ReviewResponse(updatedReview);
    }
    
    public void deleteReview(Long reviewId, Long userId) {
        logger.info("Deleting review ID: {} by user ID: {}", reviewId, userId);
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("REVIEW_NOT_FOUND", "Review not found with ID: " + reviewId));
        
        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException("UNAUTHORIZED_ACCESS", "User can only delete their own reviews");
        }
        
        Long helperId = review.getHelper().getId();
        reviewRepository.delete(review);
        
        // Update helper's rating after deletion
        updateHelperRating(helperId);
        
        logger.info("Review deleted successfully with ID: {}", reviewId);
    }
    
    public ReviewResponse hideReview(Long reviewId) {
        logger.info("Hiding review with ID: {}", reviewId);
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("REVIEW_NOT_FOUND", "Review not found with ID: " + reviewId));
        
        review.setIsVisible(false);
        Review updatedReview = reviewRepository.save(review);
        
        // Update helper's rating
        updateHelperRating(review.getHelper().getId());
        
        logger.info("Review hidden successfully with ID: {}", reviewId);
        return new ReviewResponse(updatedReview);
    }
    
    public ReviewResponse showReview(Long reviewId) {
        logger.info("Showing review with ID: {}", reviewId);
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("REVIEW_NOT_FOUND", "Review not found with ID: " + reviewId));
        
        review.setIsVisible(true);
        Review updatedReview = reviewRepository.save(review);
        
        // Update helper's rating
        updateHelperRating(review.getHelper().getId());
        
        logger.info("Review shown successfully with ID: {}", reviewId);
        return new ReviewResponse(updatedReview);
    }
    
    private void updateHelperRating(Long helperId) {
        logger.info("Updating rating for helper ID: {}", helperId);
        
        BigDecimal averageRating = reviewRepository.calculateAverageRatingByHelperId(helperId);
        Long totalReviews = reviewRepository.countVisibleReviewsByHelperId(helperId);
        
        if (averageRating == null) {
            averageRating = BigDecimal.ZERO;
        } else {
            averageRating = averageRating.setScale(2, RoundingMode.HALF_UP);
        }
        
        helperService.updateHelperRating(helperId, averageRating, totalReviews.intValue());
        
        logger.info("Helper rating updated successfully for ID: {} - Rating: {}, Total Reviews: {}", 
                   helperId, averageRating, totalReviews);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getAverageRatingByHelperId(Long helperId) {
        BigDecimal rating = reviewRepository.calculateAverageRatingByHelperId(helperId);
        return rating != null ? rating.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
    
    @Transactional(readOnly = true)
    public Long getReviewCountByHelperId(Long helperId) {
        return reviewRepository.countVisibleReviewsByHelperId(helperId);
    }
    
    @Transactional(readOnly = true)
    public Long getReviewCountByRating(Integer rating) {
        return reviewRepository.countByRating(rating);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalReviewCount() {
        return reviewRepository.count();
    }
}