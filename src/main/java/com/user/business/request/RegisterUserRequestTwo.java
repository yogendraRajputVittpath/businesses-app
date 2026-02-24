package com.user.business.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.papertrading.service.util.Constants;
import com.user.business.service.util.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequestTwo {

	@Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
	 
	@Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull(message = "Invalid DOB")
    private LocalDate dob;
    
    @JsonProperty(defaultValue = "Other")
    @Pattern(
	    regexp = Constants.GENDER_REGEX,
	    message = "Gender must be Male, Female, or Other"
	)
    private String gender;
}
