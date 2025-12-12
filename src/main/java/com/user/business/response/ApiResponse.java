package com.user.business.response;

import java.util.List;

import com.user.business.entity.User;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private String status;
    private int code;
    private String message;
    private T data;
   
    public ApiResponse(String status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
      
    }

	
}
