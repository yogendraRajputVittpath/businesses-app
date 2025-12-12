package com.user.business.exception;

public class TokenExpiredException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2133106129874207710L;

	public TokenExpiredException(String message) {
        super(message);
    }
}