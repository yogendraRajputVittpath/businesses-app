package com.user.business.service.impl.socialmedia;

import java.time.LocalDate;
import org.springframework.stereotype.Service;

import com.user.business.entity.socialmedia.SocialMedia;
import com.user.business.exception.socialmedia.CustomException;
import com.user.business.repository.socialmedia.SocialMediaRepository;
import com.user.business.request.socialmedai.SocialMediaAddRequest;
import com.user.business.request.socialmedai.SocialMediaUpdateRequest;
import com.user.business.security.JwtUtil;
import com.user.business.service.socialmedia.SocialMediaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialMediaServiceImpl implements SocialMediaService {

    private final SocialMediaRepository repository;
    private final JwtUtil jwtUtil;

    private static final String[] ALLOWED_ACCOUNTS = {
            "YOUTUBE", "INSTA", "FACEBOOK", "TWITTER"
    };

    private boolean isValidAccount(String account) {
        for (String a : ALLOWED_ACCOUNTS) {
            if (a.equalsIgnoreCase(account)) return true;
        }
        return false;
    }

    @Override

    public SocialMedia addSocialMedia(String token, SocialMediaAddRequest request) {
//        Integer userId = jwtUtil.extractUserId();
    	Long userId = jwtUtil.extractUserId(token);

        if (!isValidAccount(request.getAccount())) {
            throw new CustomException("Invalid account type. Allowed: YOUTUBE, INSTA, FACEBOOK, TWITTER");
        }

        if (repository.existsByUserIdAndAccountIgnoreCase(userId.intValue(),request.getAccount())) {
            throw new CustomException("This social media account already exists for this user.");
        }

        SocialMedia social = new SocialMedia();
//        social.setUserId(userId);
        social.setUserId(userId.intValue());
        social.setAccount(request.getAccount().toUpperCase());
        social.setLink(request.getLink());
        social.setFollowers(request.getFollowers());
        social.setTotalViews(request.getTotalViews());
        social.setTotalPosts(request.getTotalPosts());
        social.setTotalLikes(request.getTotalLikes());
        social.setAccountCreationDate(LocalDate.now());
        social.setStatus("ACTIVE");

        return repository.save(social);
    }

    @Override
    public SocialMedia updateSocialMedia(SocialMediaUpdateRequest request) {

        SocialMedia social = repository.findById(request.getId())
                .orElseThrow(() -> new CustomException("Social Media record not found"));

        if (request.getAccount() != null) {
            if (!isValidAccount(request.getAccount())) {
                throw new CustomException("Invalid account type.");
            }
            social.setAccount(request.getAccount().toUpperCase());
        }

        if (request.getLink() != null) social.setLink(request.getLink());
        if (request.getFollowers() != null) social.setFollowers(request.getFollowers());
        if (request.getTotalViews() != null) social.setTotalViews(request.getTotalViews());
        if (request.getTotalPosts() != null) social.setTotalPosts(request.getTotalPosts());
        if (request.getTotalLikes() != null) social.setTotalLikes(request.getTotalLikes());

        return repository.save(social);
    }

        @Override
    public void removeSocialMedia(Integer id) {

        SocialMedia social = repository.findById(id)
                .orElseThrow(() -> new CustomException("Social Media record not found"));

        social.setStatus("REMOVED");

        repository.save(social);
    }
}
