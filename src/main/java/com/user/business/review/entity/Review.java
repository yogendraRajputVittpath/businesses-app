package com.user.business.review.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table(name = "reviews_given_by_influencer")
@Table(name = "reviews_given_by_business")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private Long userId; // logged-in user (from JWT)

    @Column(nullable = false)
    private Long reviewGivenTo;

    @Column(length = 300, nullable = false)
//    private String message;
    private String feedback;

    @Column(nullable = false)
    private Integer rating;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
