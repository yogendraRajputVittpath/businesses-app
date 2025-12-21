package com.user.business.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.user.business.review.request.*;
import com.user.business.review.service.ReviewService;
import com.user.business.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(
            @Valid @RequestBody ReviewAddRequest request,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = jwtUtil.extractUserId(authHeader);

//        log.info("API HIT → /review/add | userId={}", userId);
        return ResponseEntity.ok(reviewService.addReview(request, userId));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview(
            @Valid @RequestBody ReviewUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = jwtUtil.extractUserId(authHeader);

//        log.info("API HIT → /review/update | userId={}", userId);
        return ResponseEntity.ok(reviewService.updateReview(request, userId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(
            @RequestParam Long reviewId,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = jwtUtil.extractUserId(authHeader);

//        log.info("API HIT → /review/delete | userId={}", userId);
        return ResponseEntity.ok(reviewService.deleteReview(reviewId, userId));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllReviews(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = jwtUtil.extractUserId(authHeader);

//        log.info("API HIT → /review/get | userId={}", userId);
//        return ResponseEntity.ok(reviewService.getAllReviews(userId));
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}
