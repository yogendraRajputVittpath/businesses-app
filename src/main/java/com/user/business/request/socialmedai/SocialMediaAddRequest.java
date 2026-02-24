package com.user.business.request.socialmedai;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SocialMediaAddRequest {
	
//	 @NotNull(message = "id is required")
//	    private Integer id;
 
	    @NotBlank(message = "account is required (YOUTUBE/INSTA/FACEBOOK/TWITTER)")
	    @Pattern(
	        regexp = "YOUTUBE|INSTA|FACEBOOK|TWITTER",
	        message = "account must be YOUTUBE, INSTA, FACEBOOK or TWITTER"
	    )
	    private String accountType;

	    @NotBlank(message = "link is required")
	    @Pattern(
	        regexp = "^(https?:\\/\\/)?(www\\.)?"
	               + "(youtube\\.com|youtu\\.be|instagram\\.com|facebook\\.com|x\\.com|twitter\\.com)\\/.+$",
	        message = "Invalid social media link"
	    )
	    private String link;

	    @Min(value = 10, message = "followers must be at least 10")
	    private Long followers;

	    @Min(value = 10, message = "totalViews must be at least 10")
	    private Long totalViews;

	    private Long totalPosts;
	    private Long totalLikes;
	}
