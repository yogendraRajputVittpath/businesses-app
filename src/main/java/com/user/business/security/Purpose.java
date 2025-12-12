package com.user.business.security;

public enum Purpose {
	
	AccountVerification("AccountVerification"),
	ForgetPassword("ForgetPassword");
	
	private final String purpose;
	
	Purpose(String purpose){
		this.purpose = purpose;
	}
	
	public String getPurpose() {
		return purpose;
	}
}
