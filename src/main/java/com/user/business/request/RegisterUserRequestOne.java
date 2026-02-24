package com.user.business.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterUserRequestOne {

	@Pattern(regexp = "^[A-Za-z]+$", message = "Invalid First Name")
    private String firstName;
	
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Last Name")
    private String lastName; // * represents Optional

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String phoneNo;
}
