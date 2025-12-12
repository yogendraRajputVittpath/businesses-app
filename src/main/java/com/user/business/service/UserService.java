package com.user.business.service;

//import com.user.details.request.ProfilePicRequest;

import org.springframework.web.multipart.MultipartFile;

import com.user.business.entity.User;
import com.user.business.request.*;
import com.user.business.response.ApiResponse;

import jakarta.validation.Valid;

public interface UserService {
	
    ApiResponse<?> registerUser(RegisterUserRequest request);
    ApiResponse<?> sendEmailOtp(EmailOtpRequest request);
    ApiResponse<?> verifyEmailOtp(VerifyOtpRequest request);
    ApiResponse<?> loginUser(LoginRequest request);
    ApiResponse<?> logoutUser(String token);
//    ApiResponse<?> editProfile(Long userId, EditProfileRequest request, String token);
    ApiResponse<?> editProfile(EditProfileRequest request, String token);
    ApiResponse<?> updatePassword(String token, UpdatePasswordRequest request);
    ApiResponse<?> forgetPassword(ForgotPasswordRequest request);
    
    ApiResponse<?> getProfile(String token);
    
    ApiResponse<?> filterProfiles(String token, FilterProfileRequest request);
    
}

