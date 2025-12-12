package com.user.business.service.util;

public class Constants {
//	public final static int 
	public final static String FAILURE = "FAILURE";
	public final static String SUCCESS = "SUCCESS";
	public final static String INVALID_Details = "INVALID_Details";
	public final static String Invalid_OTP = "Invalid OTP";
	public final static String USER_NOT_FOUND = "USER_NOT_FOUND";
	public final static String User_Not_Verified = "User_Not_Verified";
	public final static String Incomplete_Information = "Incomplete_Information";
	public final static String ACCOUNT_VERIFICATION = "AccountVerification";
    public static final int OTP_LENGTH = 6;
    
    
    /** Regex */
    
    public static final String GENDER_REGEX = "(?i)^(male|female|other)$";
    public static final String PURPOSE_REGEX = "^(AccountVerification|ForgetPassword)$";
}
