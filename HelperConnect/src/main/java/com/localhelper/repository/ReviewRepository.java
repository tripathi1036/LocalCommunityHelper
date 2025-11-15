package com.localhelper.repository;

import com.localhelper.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByUserId(Long userId);
    
    Page<Review> findByUserId(Long userId, Pageable pageable);
    
    List<Review> findByHelperId(Long helperId);
    
    Page<Review> findByHelperId(Long helperId, Pageable pageable);
    
    List<Review> findByServiceRequestId(Long serviceRequestId);
    
    List<Review> findByRating(Integer rating);
    
    Page<Review> findByRating(Integer rating, Pageable pageable);
    
    List<Review> findByIsVisible(Boolean isVisible);
    
    Page<Review> findByIsVisible(Boolean isVisible, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.helper.id = :helperId AND r.isVisible = :isVisible")
    List<Review> findByHelperIdAndIsVisible(@Param("helperId") Long helperId, @Param("isVisible") Boolean isVisible);
    
    @Query("SELECT r FROM Review r WHERE r.helper.id = :helperId AND r.isVisible = :isVisible")
    Page<Review> findByHelperIdAndIsVisible(@Param("helperId") Long helperId, @Param("isVisible") Boolean isVisible, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.helper.id = :helperId AND r.rating >= :minRating AND r.isVisible = true")
    List<Review> findByHelperIdAndRatingGreaterThanEqual(@Param("helperId") Long helperId, @Param("minRating") Integer minRating);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.helper.id = :helperId AND r.isVisible = true")
    BigDecimal calculateAverageRatingByHelperId(@Param("helperId") Long helperId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.helper.id = :helperId AND r.isVisible = true")
    Long countVisibleReviewsByHelperId(@Param("helperId") Long helperId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.helper.id = :helperId AND r.rating = :rating AND r.isVisible = true")
    Long countReviewsByHelperIdAndRating(@Param("helperId") Long helperId, @Param("rating") Integer rating);
    
    @Query("SELECT r FROM Review r WHERE r.comment IS NOT NULL AND r.comment != '' AND r.isVisible = true")
    Page<Review> findReviewsWithComments(Pageable pageable);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.rating = :rating")
    Long countByRating(@Param("rating") Integer rating);
}