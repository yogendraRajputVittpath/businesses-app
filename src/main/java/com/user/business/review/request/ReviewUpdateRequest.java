package com.user.business.review.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewUpdateRequest {

    @NotNull(message = "ReviewId is required")
    private Long reviewId;

//    @NotBlank(message = "Message is required")
//    @Pattern(
//        regexp = "^[a-zA-Z0-9 ,.?!@#&()_-]{5,300}$",
//        message = "Message must be 5-300 characters and valid text only"
//    )
//    private String message;
    @NotBlank(message = "Feedback is required")
    @Size(min = 5, max = 300, message = "Feedback must be between 5 and 300 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9 ,.?!@#&()_-]+$",
        message = "Feedback contains invalid characters"
    )
    private String feedback;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be >= 1")
    @Max(value = 5, message = "Rating must be <= 5")
    private Integer rating;
}
