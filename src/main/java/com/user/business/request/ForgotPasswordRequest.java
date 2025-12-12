package com.user.business.request;

//ForgotPasswordRequest.java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String otp;
	 
	@NotBlank
	private String newPassword;
	 
	 
}
