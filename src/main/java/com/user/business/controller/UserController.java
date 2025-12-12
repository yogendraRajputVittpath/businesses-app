package com.user.business.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.business.request.EditProfileRequest;
import com.user.business.request.EmailOtpRequest;
import com.user.business.request.FilterProfileRequest;
import com.user.business.request.ForgotPasswordRequest;
import com.user.business.request.LoginRequest;
import com.user.business.request.ProfilePicRequest;
import com.user.business.request.RegisterUserRequest;
import com.user.business.request.UpdatePasswordRequest;
import com.user.business.request.VerifyOtpRequest;
import com.user.business.response.ApiResponse;
import com.user.business.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logoutUser(
            @RequestHeader("Authorization") String bearerToken
    ) {
        return ResponseEntity.ok(userService.logoutUser(bearerToken));
    }


    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<?>> sendOtp(@Valid @RequestBody EmailOtpRequest request) {
        return ResponseEntity.ok(userService.sendEmailOtp(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(userService.verifyEmailOtp(request));
    }

    @PostMapping("/update-password")
    public ResponseEntity<ApiResponse<?>> updatePassword(
            @RequestHeader("Authorization") String bearerToken,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        return ResponseEntity.ok(userService.updatePassword(bearerToken, request));
    }
    
    @PostMapping("/forget-password")
	public ResponseEntity<?> forgetPassword(
			@Valid 
			@RequestBody ForgotPasswordRequest request) {
    	return ResponseEntity.ok(userService.forgetPassword(request));
    }
    
//    @PutMapping("/{userId}/edit-profile")
    @PutMapping("edit-profile")
    public ResponseEntity<ApiResponse<?>> editProfile(
//            @PathVariable Long userId,
            @RequestBody EditProfileRequest request,
//            @RequestHeader("token") String token) {
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.editProfile(request, token));
    }
    
    @GetMapping("/get-profile")
    public ResponseEntity<ApiResponse<?>> getProfile(
    		@RequestHeader("Authorization") String bearerToken) {
    	System.out.println("1");
        return ResponseEntity.ok(userService.getProfile(bearerToken));
    }
    
    
    @PostMapping("/getProfile/filter")
    public ApiResponse<?> filterProfiles(
            @RequestHeader("Authorization") String token,
            @RequestBody FilterProfileRequest request) {

        return userService.filterProfiles(token, request);
    }

    
 
    @GetMapping("/check_token")
    public boolean checkToken(String token) {
       
	   return true;
    }

}
