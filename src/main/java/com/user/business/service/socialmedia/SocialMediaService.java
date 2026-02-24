package com.user.business.service.socialmedia;

import com.user.business.entity.socialmedia.SocialMedia;
import com.user.business.request.socialmedai.SocialMediaAddRequest;
import com.user.business.request.socialmedai.SocialMediaUpdateRequest;
import com.user.business.response.ApiResponse;

public interface SocialMediaService {

	SocialMedia addSocialMedia(String token, SocialMediaAddRequest request);

	public SocialMedia updateSocialMedia(String token,SocialMediaUpdateRequest request);
	
    void removeSocialMedia(String token,Integer id);
    
    ApiResponse<?> getSocialMediaByUserId(String token);
}

