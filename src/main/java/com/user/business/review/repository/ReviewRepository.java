package com.user.business.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.business.review.entity.Review;
import com.user.business.review.entity.ReviewStatus;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewIdAndUserIdAndStatus(
            Long reviewId, Long userId, ReviewStatus status);

    List<Review> findByStatus(ReviewStatus status);
}
