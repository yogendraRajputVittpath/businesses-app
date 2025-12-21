package com.user.business.review.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.user.business.review.entity.Review;
import com.user.business.review.entity.ReviewStatus;
import com.user.business.review.repository.ReviewRepository;
import com.user.business.review.request.*;
import com.user.business.review.service.ReviewService;
import com.user.business.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public ApiResponse<?> addReview(ReviewAddRequest request, Long userId) {

        log.info("Add Review | userId={} | reviewGivenTo={}", userId, request.getReviewGivenTo());

        Review review = Review.builder()
                .userId(userId)
                .reviewGivenTo(request.getReviewGivenTo())
//                .message(request.getMessage())
                .feedback(request.getFeedback())
                .rating(request.getRating())
                .status(ReviewStatus.ACTIVE)
                .createdDate(LocalDateTime.now())
                .build();

//        reviewRepository.save(review);
        Review savedReview = reviewRepository.save(review);

        return ApiResponse.builder()
                .status("SUCCESS")
                .code(200)
                .message("Feedback added successfully")
//                .data(null)
                .data(savedReview) 
                .build();
    }

    @Override
    public ApiResponse<?> updateReview(ReviewUpdateRequest request, Long userId) {

        log.info("Update Review | userId={} | reviewId={}", userId, request.getReviewId());

        Review review = reviewRepository
                .findByReviewIdAndUserIdAndStatus(
                        request.getReviewId(), userId, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Unauthorized Access"));

//        review.setMessage(request.getMessage());
        review.setFeedback(request.getFeedback());
        review.setRating(request.getRating());
        review.setModifiedDate(LocalDateTime.now());

//        reviewRepository.save(review);
        Review updatedReview = reviewRepository.save(review);

        return ApiResponse.builder()
                .status("SUCCESS")
                .code(200)
                .message("Feedback Updated Successfully")
//                .data(null)
                .data(updatedReview)
                .build();
    }

    @Override
    public ApiResponse<?> deleteReview(Long reviewId, Long userId) {

        log.info("Delete Review | userId={} | reviewId={}", userId, reviewId);

        Review review = reviewRepository
                .findByReviewIdAndUserIdAndStatus(
                        reviewId, userId, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Unauthorized Access"));

        review.setStatus(ReviewStatus.DELETED);
        review.setModifiedDate(LocalDateTime.now());

//        reviewRepository.save(review);
        Review deletedReview = reviewRepository.save(review);

        return ApiResponse.builder()
                .status("SUCCESS")
                .code(200)
                .message("Feedback deleted Successfully")
//                .data(null)
                .data(deletedReview)
                .build();
    }

    @Override
    public ApiResponse<?> getAllReviews() {

        log.info("Fetching all ACTIVE reviews");

        List<Review> reviews = reviewRepository.findByStatus(ReviewStatus.ACTIVE);

        return ApiResponse.builder()
                .status("SUCCESS")
                .code(200)
                .message("Reviews fetched successfully")
                .data(reviews)
                .build();
    }
}
