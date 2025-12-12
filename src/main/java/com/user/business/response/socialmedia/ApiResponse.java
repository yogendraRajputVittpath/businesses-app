
package com.user.business.response.socialmedia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;   // SUCCESS / FAILURE
    private Integer code;    // HTTP or custom code
    private String message;
    private T data;
}
