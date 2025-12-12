package com.user.business.exception;

public class FileStorageException extends RuntimeException {
    
	private static final long serialVersionUID = 4853440781557333967L;

	public FileStorageException(String message) {
        super(message);
    }
}