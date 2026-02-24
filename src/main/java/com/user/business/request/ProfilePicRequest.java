//package com.user.business.request;
//
//import org.springframework.web.multipart.MultipartFile;
//
////import jakarta.validation.constraints.NotBlank;
//import lombok.Data;
//
//@Data
//public class ProfilePicRequest {
//	
////   @NotBlank(message = "Profile picture is required")
//    private MultipartFile profilePic;
//
//}

package com.user.business.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProfilePicRequest {
	
//   @NotBlank(message = "Profile picture is required")
    private MultipartFile profilePic;

}