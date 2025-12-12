package com.user.business.request.socialmedai;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SocialMediaUpdateRequest {

    @NotNull(message = "id is required")
    private Integer id;
    private String account;
    private String link;
    private Long followers;
    private Long totalViews;
    private Long totalPosts;
    private Long totalLikes;
}
