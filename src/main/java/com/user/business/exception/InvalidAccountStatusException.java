package com.user.business.exception;

public class InvalidAccountStatusException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public InvalidAccountStatusException(String message) {
        super(message);
    }
}
