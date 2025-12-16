package com.user.business.service.socialmedia;

import com.user.business.entity.socialmedia.SocialMedia;
import com.user.business.request.socialmedai.SocialMediaAddRequest;
import com.user.business.request.socialmedai.SocialMediaUpdateRequest;
import com.user.business.response.ApiResponse;

public interface SocialMediaService {

//    SocialMedia addSocialMedia(SocialMediaAddRequest request);
	SocialMedia addSocialMedia(String token, SocialMediaAddRequest request);

//    SocialMedia updateSocialMedia(String token, SocialMediaUpdateRequest request);
	SocialMedia updateSocialMedia(SocialMediaUpdateRequest request);
    void removeSocialMedia(Integer id);
    
    ApiResponse<?> getSocialMediaByUserId(String token);
}
