package com.user.business.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordVerifyRequest {

//    @NotBlank(message = "UserId is required")
//    private String userId;

    @NotBlank(message = "Otp is required")
    private String otp;

    @NotBlank(message = "New password is required")
    private String newPassword;
}
