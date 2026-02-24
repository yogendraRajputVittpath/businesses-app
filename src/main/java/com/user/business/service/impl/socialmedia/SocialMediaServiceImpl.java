package com.user.business.service.impl.socialmedia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.user.business.entity.socialmedia.SocialMedia;
import com.user.business.exception.socialmedia.CustomException;
import com.user.business.repository.socialmedia.SocialMediaRepository;
import com.user.business.request.socialmedai.SocialMediaAddRequest;
import com.user.business.request.socialmedai.SocialMediaUpdateRequest;
import com.user.business.response.ApiResponse;
import com.user.business.security.JwtUtil;
import com.user.business.service.constant.ServiceConstants;
import com.user.business.service.socialmedia.SocialMediaService;
import com.user.business.service.util.Constants;
//import com.user.details.service.constant.ServiceConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialMediaServiceImpl implements SocialMediaService {

    private final SocialMediaRepository repository;
    private final JwtUtil jwtUtil;

    private boolean isValidAccount(String account) {
        return ServiceConstants.YOUTUBE.equalsIgnoreCase(account)
            || ServiceConstants.INSTA.equalsIgnoreCase(account)
            || ServiceConstants.FACEBOOK.equalsIgnoreCase(account)
            || ServiceConstants.TWITTER.equalsIgnoreCase(account);
    }

    private void validateAccountAndLink(String account, String link) {

        if (ServiceConstants.YOUTUBE.equalsIgnoreCase(account)
                && !(link.contains("youtube.com") || link.contains("youtu.be"))) {
            throw new CustomException("YouTube account requires YouTube link");
        }

        if (ServiceConstants.INSTA.equalsIgnoreCase(account)
                && !link.contains("instagram.com")) {
            throw new CustomException("Instagram account requires Instagram link");
        }

        if (ServiceConstants.FACEBOOK.equalsIgnoreCase(account)
                && !link.contains("facebook.com")) {
            throw new CustomException("Facebook account requires Facebook link");
        }

        if (ServiceConstants.TWITTER.equalsIgnoreCase(account)
                && !(link.contains("twitter.com") || link.contains("x.com"))) {
            throw new CustomException("Twitter account requires Twitter/X link");
        }
    }

    @Override
    public SocialMedia addSocialMedia(String token, SocialMediaAddRequest request) {

        Long userId = jwtUtil.extractUserId(token);

        if (!isValidAccount(request.getAccountType())) {
            throw new CustomException("Invalid social media account");
        }

        validateAccountAndLink(request.getAccountType(), request.getLink());

        if (repository.existsByUserIdAndAccountIgnoreCaseAndStatus(
                userId.intValue(),
                request.getAccountType(),
                ServiceConstants.ACTIVE)) {
            throw new CustomException("Social media account already added");
        }

        SocialMedia social = new SocialMedia();
        social.setUserId(userId.intValue());
        social.setAccount(request.getAccountType().toUpperCase());
        social.setLink(request.getLink());
        social.setFollowers(request.getFollowers());
        social.setTotalViews(request.getTotalViews());
        social.setTotalPosts(request.getTotalPosts());
        social.setTotalLikes(request.getTotalLikes());
        social.setAccountCreationDate(LocalDate.now());
        social.setStatus(ServiceConstants.ACTIVE);

        return repository.save(social);
    }

    @Override
    public SocialMedia updateSocialMedia(String token, SocialMediaUpdateRequest request) {

        Long userId = jwtUtil.extractUserId(token);

        SocialMedia social = repository.findById(request.getId())
                .orElseThrow(() -> new CustomException("Record not found"));

        if (!social.getUserId().equals(userId.intValue())) {
            throw new CustomException("Unauthorized update");
        }

        if (request.getAccountType() != null &&
            !request.getAccountType().equalsIgnoreCase(social.getAccount())) {
            throw new CustomException("Account type cannot be changed");
        }

        String finalLink = request.getLink() != null
                ? request.getLink()
                : social.getLink();

        validateAccountAndLink(social.getAccount(), finalLink);

        if (request.getLink() != null) {
            social.setLink(request.getLink());
        }

        if (request.getFollowers() != null) social.setFollowers(request.getFollowers());
        if (request.getTotalViews() != null) social.setTotalViews(request.getTotalViews());
        if (request.getTotalPosts() != null) social.setTotalPosts(request.getTotalPosts());
        if (request.getTotalLikes() != null) social.setTotalLikes(request.getTotalLikes());

        return repository.save(social);
    }

    @Override
    public void removeSocialMedia(String token, Integer id) {

        Long userId = jwtUtil.extractUserId(token);

        SocialMedia social = repository.findById(id)
                .orElseThrow(() -> new CustomException("Record not found"));

        if (!social.getUserId().equals(userId.intValue())) {
            throw new CustomException("Unauthorized delete");
        }

        social.setStatus(ServiceConstants.REMOVED);
        repository.save(social);
    }

    @Override
    public ApiResponse<?> getSocialMediaByUserId(String token) {

        Long userId = jwtUtil.extractUserId(token);
        
        if (userId == null) {
          return new ApiResponse<>(
                  Constants.FAILURE,
                  401,
                  "Invalid token",
                  null
          );
        }

        List<SocialMedia> list = repository.findByUserId(userId);

        return new ApiResponse<>(
                Constants.SUCCESS,
                200,
                "Data fetched successfully",
                list
        );
    }
}


