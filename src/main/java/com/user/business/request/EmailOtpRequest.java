package com.user.business.request;

import com.user.business.service.util.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailOtpRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
//    
//    @NotNull(message = "userId is required")
//    private Long userId;
//    
    @Pattern(
            regexp = Constants.PURPOSE_REGEX,
            message = "Purpose must be either AccountVerification or PasswordReset or ForgetPassword"
        )
    private String purpose;

}
