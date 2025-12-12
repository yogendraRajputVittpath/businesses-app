
package com.user.business.request.socialmedai;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SocialMediaAddRequest {

//    @NotNull(message = "userId is required")
//    private Integer userId;

    @NotBlank(message = "account is required (YOUTUBE/INSTA/FACEBOOK/TWITTER)")
    private String account;

    @NotBlank(message = "link is required")
    private String link;

    // optional fields
    private Long followers;
    private LocalDate accountCreationDate;
    private Long totalViews;
    private Long totalPosts;
    private Long totalLikes;
}
