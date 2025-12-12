package com.user.business.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponse {
	private String userStatus;
	private Long userId;
}
