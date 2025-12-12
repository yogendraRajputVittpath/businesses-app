package com.user.business.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.business.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("Validation failed: {}", errorMessage);
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<Object>("FAILURE", 400, errorMessage,null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
    	log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<Object>("FAILURE", 500, ex.getMessage(), null));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
    	log.error("exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<Object>("FAILURE", 500, "Something Went Wrong... ", null));
    }
    
 // 🟢 Handle Token Expired Exception
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleTokenExpired(TokenExpiredException ex) {
    	log.info("Returning JsonValue if Token Expire...");
    	
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "FAILURE");
        errorResponse.put("code", 401);
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("data", null);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
