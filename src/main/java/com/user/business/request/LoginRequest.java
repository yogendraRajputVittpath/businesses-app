package com.user.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LoginRequest {

//
//	@NotNull(message = "UserId must not be null")
//	private Long userId;


	@NotNull(message = "Email must not be null")
	private String Email;

    @NotBlank(message = "Password must not be null")
    private String password;
}
