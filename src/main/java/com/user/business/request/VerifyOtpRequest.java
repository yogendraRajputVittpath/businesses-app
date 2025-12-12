package com.user.business.request;

import com.user.business.service.util.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "OTP is required")
    private String otp;

    @NotBlank(message = "Purpose is required")
    @Pattern(
            regexp = Constants.PURPOSE_REGEX,
            message = "Purpose must be either AccountVerification or ForgetPassword"
        )
    private String purpose; // AccountVerification or ForgetPassword
   
}
