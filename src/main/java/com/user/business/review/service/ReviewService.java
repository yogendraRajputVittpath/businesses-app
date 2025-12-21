package com.user.business.review.service;

import com.user.business.review.request.ReviewAddRequest;
import com.user.business.review.request.ReviewUpdateRequest;
import com.user.business.response.ApiResponse;

public interface ReviewService {

    ApiResponse<?> addReview(ReviewAddRequest request, Long userId);

    ApiResponse<?> updateReview(ReviewUpdateRequest request, Long userId);

    ApiResponse<?> deleteReview(Long reviewId, Long userId);

    ApiResponse<?> getAllReviews();
}
