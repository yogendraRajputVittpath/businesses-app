package com.user.business.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

//    @NotBlank(message = "UserId is required")
//    private String userId;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
