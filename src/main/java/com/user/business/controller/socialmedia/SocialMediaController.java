package com.user.business.controller.socialmedia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.business.entity.socialmedia.SocialMedia;
import com.user.business.request.socialmedai.SocialMediaAddRequest;
import com.user.business.request.socialmedai.SocialMediaUpdateRequest;
import com.user.business.response.ApiResponse;
import com.user.business.service.socialmedia.SocialMediaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/socialMedia")
@RequiredArgsConstructor
public class SocialMediaController {

    private final SocialMediaService service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addSocial(
            @Valid @RequestBody SocialMediaAddRequest request,
            @RequestHeader("Authorization") String token) {


    	SocialMedia data = service.addSocialMedia(token, request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", 200, "Record Added Successfully", data));
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateSocial(
            @Valid @RequestBody SocialMediaUpdateRequest request) {

        SocialMedia data = service.updateSocialMedia(request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", 200, "Record Updated Successfully", data));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<?>> removeSocial(@PathVariable Integer id) {

        service.removeSocialMedia(id);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", 200, "Account Marked as REMOVED", null));
    }
}
