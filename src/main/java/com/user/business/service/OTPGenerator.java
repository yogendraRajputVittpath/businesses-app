package com.user.business.service;

import java.security.SecureRandom;
import java.util.stream.Collectors;

import com.user.business.service.util.Constants;

public class OTPGenerator {
	private static final String CHARACTERS = "0123456789";
	private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
    	return random.ints(Constants.OTP_LENGTH, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
    
}
