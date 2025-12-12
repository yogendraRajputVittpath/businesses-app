package com.user.business.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpResponse {
    private String email;
    private String otp;
    private String purpose;   // REGISTER / FORGOT_PASSWORD / LOGIN
    private boolean verified;
}
