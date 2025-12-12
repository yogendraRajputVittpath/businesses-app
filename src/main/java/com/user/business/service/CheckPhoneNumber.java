package com.user.business.service;

public class CheckPhoneNumber {
	public static boolean isValidOrNot(Long phoneNumber) {
		// Convert long to String for pattern matching
	    String phoneStr = String.valueOf(phoneNumber);

	    // Check length and first digit
	    return phoneStr.matches("^[5-9]\\d{9}$");
	}
	

}
