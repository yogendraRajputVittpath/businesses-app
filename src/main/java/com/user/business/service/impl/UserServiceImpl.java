package com.user.business.service.impl;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.user.business.entity.EmailOtp;
import com.user.business.entity.PasswordResetToken;
import com.user.business.entity.User;
import com.user.business.entity.UserStatus;
import com.user.business.entity.UserToken;
import com.user.business.exception.FileStorageException;
import com.user.business.exception.InvalidAccountStatusException;
import com.user.business.repository.EmailOtpRepository;
import com.user.business.repository.PasswordResetTokenRepository;
import com.user.business.repository.UserRepository;
import com.user.business.repository.UserTokenRepository;
import com.user.business.request.*;
import com.user.business.response.ApiResponse;
import com.user.business.response.RegistrationResponse;
import com.user.business.response.TokenResponse;
import com.user.business.security.JwtUtil;
import com.user.business.security.PasswordUtils;
import com.user.business.security.Purpose;
import com.user.business.service.CheckPhoneNumber;
import com.user.business.service.EmailService;
import com.user.business.service.OTPGenerator;
import com.user.business.service.UserService;
import com.user.business.service.util.Constants;
//import com.user.details.request.RegisterUserRequestOne;
//import com.user.details.request.RegisterUserRequestTwo;
import com.user.business.request.EmailOtpRequest;
import com.user.business.request.FilterProfileRequest;
import com.user.business.request.ForgotPasswordRequest;
import com.user.business.request.LoginRequest;
import com.user.business.request.UpdatePasswordRequest;
import com.user.business.request.VerifyOtpRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailOtpRepository emailOtpRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UploadingImage uploadingImage;

    private static Long phoneNumber;

    @Value("${enable.password.encryption}")
    private boolean encryptPasswordEnable;

    @Value("${enable.otp.encryption}")
    private boolean encryptOtpEnable;

    @Value("${token.expiary.time.hours}")
    private int expiaryTime;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Override
    public ApiResponse<?> registerUserOne(RegisterUserRequestOne request) {
        try {
            log.info(":: [Step 1] Registration started for Email: {} and Phone: {}", request.getEmail(), request.getPhoneNo());

            User user;
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                user = existingUser.get();
                log.warn(":: User already exists with Status: {}", user.getStatus());

                if (UserStatus.ACTIVE.name().equals(user.getStatus())) {
                    log.error(":: User is already ACTIVE. Redirecting to Login.");
                    return new ApiResponse<>("FAILURE", 400, "Email already registered. Please login.", null);
                }
                
                log.info(":: Updating existing user details for Step 1");
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setPhoneNo(Long.parseLong(request.getPhoneNo()));
            } else {
                log.info(":: Creating new user for Step 1");
                user = User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .phoneNo(Long.parseLong(request.getPhoneNo()))
                        .emailNotificationEnabled(true)
                        .build();
            }

            user.setStatus(UserStatus.VERIFICATION_PENDING.name());
            User savedUser = userRepository.save(user);
            
            log.info(":: [Step 1] Success. User ID: {} saved with Status: {}", savedUser.getId(), savedUser.getStatus());
            return new ApiResponse<>("SUCCESS", 200, "User Registered Successfully.", savedUser);

        } catch (Exception e) {
            log.error(":: [Step 1] Critical Failure: ", e);
            return new ApiResponse<>("FAILURE", 400, "Something went wrong in Step 1", null);
        }
    }
    
    
    
    @Override
    public ApiResponse<?> registerUserTwo(RegisterUserRequestTwo request) {
        try {
            log.info(":: [Step 2] Continuing registration for Email: {}", request.getEmail());

            // 1. Fetch User
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        log.error(":: [Step 2] User not found for Email: {}", request.getEmail());
                        return new RuntimeException("Invalid Registration Stage. Please complete Step 1.");
                    });

            // 2. Password Processing
            String rawPassword = request.getPassword();
            String passwordToSave;
            
            if (encryptPasswordEnable) {
                log.info(":: Encrypting password for user: {}", request.getEmail());
                passwordToSave = passwordEncoder.encode(rawPassword);
            } else {
                log.warn(":: Encryption DISABLED. Saving plain text password for: {}", request.getEmail());
                passwordToSave = rawPassword;
            }

            // 3. Data Mapping
            user.setPassword(passwordToSave);
            user.setDob(request.getDob());
            user.setGender(request.getGender()); // Request mein null hoga toh DB mein bhi null jayega
            user.setStatus(UserStatus.VERIFICATION_PENDING.name()); // Status abhi bhi pending rahega

            log.info(":: Updating User fields (DOB: {}, Gender: {})", user.getDob(), user.getGender());

            // 4. Final Save
            User fullyUpdatedUser = userRepository.save(user);
            log.info(":: [Step 2] Database updated successfully for User ID: {}", fullyUpdatedUser.getId());


            return new ApiResponse<>("SUCCESS", 200, "Registration Step 2 Complete.", fullyUpdatedUser);

        } catch (Exception e) {
            log.error(":: [Step 2] Critical Failure: ", e);
            return new ApiResponse<>("FAILURE", 400, e.getMessage(), null);
        }
    }
    

    @Override
    public ApiResponse<?> sendEmailOtp(EmailOtpRequest request) {
        try {
            log.info(":: sendEmailOtp Started for request {} ", request);

            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            log.info(userOpt.toString());

            if (userOpt.isEmpty()) {
                log.error("Send Otp Api Failed Because User not Found {} ");
                return new ApiResponse<>(Constants.FAILURE, 404, "User not found", null);
            }
            User user = userOpt.get();

            if (user.getEmail().equals(request.getEmail())) {
                log.info("Email Present...");
                log.info("User Present...");
            }

            if (!user.getEmail().equals(request.getEmail())) {
                return new ApiResponse<>(Constants.FAILURE,400,"Invalid Email/UserId",null);
            }

            if (Constants.ACCOUNT_VERIFICATION.equalsIgnoreCase(request.getPurpose()) &&
                    !user.getStatus().equals(UserStatus.VERIFICATION_PENDING.name())) {
                log.error("Send Otp Api Failed Because User is Already {} ", user.getStatus());
                return new ApiResponse<>(Constants.FAILURE,400,"User is already " + user.getStatus(),null);
            }
            String otpCode = OTPGenerator.generateOtp();
            String encryptedOtp = otpCode;
            log.info("New Otp : {}", otpCode);
            if (encryptOtpEnable) {
                log.info("Encrypting OTP before saving for user: {}", user.getEmail());
                encryptedOtp = passwordEncoder.encode(encryptedOtp);
                log.info("Encrypted new Otp : {}", encryptedOtp);
            } else {
                log.info("Saving OTP in plain text for user: {}", user.getEmail());
            }

            EmailOtp otp = EmailOtp.builder()
                    .user(user)
                    .email(request.getEmail())
                    .otp(encryptedOtp)
                    .purpose(request.getPurpose())
                    .status(UserStatus.ACTIVE.name())
                    .expiryTime(LocalDateTime.now().plusMinutes(15))
                    .createdAt(LocalDateTime.now())
                    .build();

            emailOtpRepository.save(otp);
            boolean emailSent = emailService.sendEmailOtp(user.getEmail(), otpCode);

            log.info(":: OTP successfully sent to email {} ", user.getEmail());
            return new ApiResponse<>(Constants.SUCCESS,200,"OTP sent successfully",null);

        } catch (Exception e) {
            log.error("Send Otp Api Failed... ", e);
            return new ApiResponse<>("FAILURE",500,"Something went wrong: ",null);
        }
    }

    @Override
    public ApiResponse<?> verifyEmailOtp(VerifyOtpRequest request) {
        try {
        	log.info("Verification : {}",request.toString());
        	
            Optional<EmailOtp> otpOpt = emailOtpRepository
                    .findTopByEmailAndPurposeAndStatusOrderByCreatedAtDesc(
                            request.getEmail(), 
                            request.getPurpose(), 
                            UserStatus.ACTIVE.name());
            
            log.info("Email_Otp Details : {}", otpOpt.toString());

            String userStatus = userRepository.findStatusByEmail(request.getEmail());
            log.info("UserStatus : {}", userStatus);

            if (Purpose.AccountVerification.getPurpose().equalsIgnoreCase(request.getPurpose())
//                    && userStatus.equals(UserStatus.ACTIVE.name())) {
            		&& UserStatus.ACTIVE.name().equals(userStatus)) {
                return new ApiResponse<>(Constants.SUCCESS,200,"Account Already Verified, Please Login...",null);
            }

            if (otpOpt.isEmpty()) {
                log.warn("Check Details...");
                return new ApiResponse<>("FAILURE",400,"Check your details and Resend OTP",null);
            } else {
                log.info("User for Account Verification : {}" + otpOpt.toString());
            }

            EmailOtp otp = otpOpt.get();
            log.info("Email_Otp : {}", otp.toString());

            if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
                otp.setStatus(UserStatus.EXPIRED.name());
                log.info("Otp has Expire and it now be saved...");
                return new ApiResponse<>("FAILURE",400,"OTP expired",null);
            }
            if (encryptOtpEnable) {
                log.info("OTP encryption ENABLED, matching encoded OTP");
                if (!passwordEncoder.matches(request.getOtp(), otp.getOtp())) {
                    log.warn("OTP verification FAILED for email: {}", request.getEmail());
                    return new ApiResponse<>(Constants.FAILURE,400,"Invalid OTP",null);
                }
            } else {
                log.info("OTP encryption DISABLED, matching plain OTP");
                if (!otp.getOtp().equals(request.getOtp())) {
                    log.warn("OTP verification FAILED for email: {}", request.getEmail());
                    return new ApiResponse<>(Constants.FAILURE,400,"Invalid OTP",null);
                }
            }

            emailOtpRepository.save(otp);

            // Handle purpose
            User user = otp.getUser();
            log.info("User Deatil : {}", user);
//
//            if (Purpose.AccountVerification.getPurpose().equalsIgnoreCase(request.getPurpose())
//                    || Purpose.ForgetPassword.getPurpose().equalsIgnoreCase(request.getPurpose())) {

                otp.setVerified(true);
                user.setStatus(UserStatus.ACTIVE.name());
                userRepository.save(user);

                emailOtpRepository.deleteOnlyOtp(request.getEmail(), request.getOtp());
            String successMessage = "OTP Verification Successful";

            log.info(successMessage);
            return new ApiResponse<>(Constants.SUCCESS, 200, successMessage, null);

        } catch (Exception e) {
            log.error("Validate Otp Api Failed... ", e);
            return new ApiResponse<>("FAILURE", 400,"Something went wrong: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<?> loginUser(LoginRequest request) {
        if (ObjectUtils.isEmpty(request.getEmail()) || ObjectUtils.isEmpty(request.getPassword())) {
            return new ApiResponse<>(Constants.FAILURE, 400, Constants.Incomplete_Information, null);
        }

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return new ApiResponse<>(Constants.FAILURE, 400, Constants.USER_NOT_FOUND, null);
        }

        User user = optionalUser.get();

        if ("VerificationPending".equalsIgnoreCase(user.getStatus())) {
            return new ApiResponse<>(Constants.FAILURE, 400, "Please complete Step 2 of registration first.", 
                RegistrationResponse.builder()
                    .userStatus("VerificationPending")
                    .userId(user.getId())
                    .build());
        }

        boolean isPasswordMatch = encryptPasswordEnable 
            ? passwordEncoder.matches(request.getPassword(), user.getPassword())
            : request.getPassword().equals(user.getPassword());

        if (!isPasswordMatch) {
            handleLoginFailed(user); // Retries increment logic
            return new ApiResponse<>(Constants.FAILURE, 400, Constants.INVALID_Details, 
                RegistrationResponse.builder().invalidAttempts(user.getLoginFailedRetries()).build());
        }

        if ("DISABLED".equalsIgnoreCase(user.getStatus())) {
            return new ApiResponse<>(Constants.FAILURE, 403, "Account is disabled. Contact Admin.", null);
        }

        try {
            String token = jwtUtil.generateToken(user.getEmail(), user.getId());
            
            user.setLoginFailedRetries(0);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            UserToken userToken = UserToken.builder()
                    .token(token)
                    .user(user)
                    .expiryTime(LocalDateTime.now().plusHours(expiaryTime)) // Ensure expiaryTime is defined
                    .build();
            userTokenRepository.save(userToken);

            log.info("User {} logged in successfully", user.getEmail());
            
            return new ApiResponse<>(Constants.SUCCESS, 200, "Logged In Successfully", 
                TokenResponse.builder()
                    .token("Bearer " + token)
                    .userId(user.getId())
                    .build());

        } catch (Exception e) {
            log.error("Token generation failed", e);
            return new ApiResponse<>(Constants.FAILURE, 500, "Internal Server Error during login", null);
        }
    }

    
    @Override
    @Transactional
    public ApiResponse<?> logoutUser(String bearerToken) {
        try {
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                return new ApiResponse<>(Constants.FAILURE,400,"Authorization token is missing or invalid format",null);
            }

            String token = bearerToken.replace("Bearer ", "").trim();
            Optional<UserToken> optionalUserToken = userTokenRepository.findByToken(token);
           if (optionalUserToken.isEmpty()) {
                return new ApiResponse<>(Constants.FAILURE,401,"Invalid or already expired token",null);
            }

            UserToken userToken = optionalUserToken.get();
            log.info("UserToken Info : {}", userToken);
            userTokenRepository.deleteByToken(token);
            log.info("Token Deleted from DB...");

            SecurityContextHolder.clearContext();
            return new ApiResponse<>(Constants.SUCCESS,200,"Logged out successfully",null);

        } catch (Exception e) {
            log.error("Logout failed", e);
            return new ApiResponse<>(Constants.FAILURE,500,"Something went wrong during logout",null);
        }
    }

    @Override
    public ApiResponse<?> updatePassword(String bearerToken, UpdatePasswordRequest request) {
        try {
            String token = bearerToken.replace("Bearer ", "").trim();

            Long userId = jwtUtil.extractUserId(token);
            log.info("UserId From Token : {}", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (encryptPasswordEnable == true) {
                if (PasswordUtils.isBCryptHash(user.getPassword())) {
                    log.info("Old password is BCrypt : {}", user.getPassword());
                    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                        return new ApiResponse<>("FAILURE",400,"CurrentPassword is wrong",null);
                    }
                } else {
                    log.info("Old password is Not BCrypt : {}", user.getPassword());
                    if (!user.getPassword().equals(request.getCurrentPassword())) {
                        return new ApiResponse<>("FAILURE",400,"CurrentPassword is wrong",null);
                    }
                }
            }
            if (PasswordUtils.isBCryptHash(user.getPassword())) {
                log.info("Old password is BCrypt : {}", user.getPassword());
                if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                    return new ApiResponse<>("FAILURE",400,"New Password is Same As Old...",null);
                }
            } else {
                log.info("Old password is Not BCrypt : {}", user.getPassword());
                if (user.getPassword().equals(request.getNewPassword())) {
                    return new ApiResponse<>("FAILURE",400,"New Password is Same As Old...",null);
                }
            }
            log.info("Old Password : {}", user.getPassword());
            log.info("New Password : {}", request.getNewPassword());

            return changeUserPassword(user, request.getNewPassword(), false);
           } catch (Exception e) {
            return new ApiResponse<>("FAILURE",400,"Something went wrong: " + e.getMessage(),null);
        }
    }

    @Override
    public ApiResponse<?> editProfile(EditProfileRequest request, String token) {

        log.info("Edit profile request received.");

        try {
            String cleanToken = token.replace("Bearer ", "").trim();
            String email = jwtUtil.extractEmail(cleanToken);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ----------------------------------------
            // ✔ SIMPLE LINE-BY-LINE FIELD UPDATION
            // ----------------------------------------

            user.setBusinessName(request.getBusinessName());
            user.setUserName(request.getUserName());
            user.setWebsite(request.getWebsite());

            if (request.getRegisteredDate() != null) {
                if (request.getRegisteredDate().isAfter(LocalDate.now())) {
                    return new ApiResponse<>(Constants.FAILURE, 400,
                            "Registered date cannot be in the future", null);
                }
                user.setRegisteredDate(request.getRegisteredDate());
            }

//            user.setPhoneNo(request.getPhoneNo());
            user.setAlternatePhone(request.getAlternatePhone());
            user.setLogo(request.getLogo());
            user.setTurnover(request.getTurnover());
            user.setPanCardNo(request.getPanCardNo());

            user.setIndustry(request.getIndustry());
//            user.setStatus(request.getStatus());
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                try {
                    user.setStatus(request.getStatus());
                } catch (IllegalArgumentException e) {
                    return new ApiResponse<>(Constants.FAILURE, 400, "Invalid status value", null);
                }
            }


            user.setRegisteredAddress(request.getRegisteredAddress());
            user.setCity(request.getCity());
            user.setState(request.getState());
            user.setPincode(request.getPincode());
            user.setCin(request.getCin());
            user.setGst(request.getGst());

            // ----------------------------------------

            userRepository.save(user);

            return new ApiResponse<>(Constants.SUCCESS, 200,
                    "Profile Updated Successfully", null);

        } catch (Exception ex) {
            log.error("Error updating profile :: {}", ex.getMessage());
            return new ApiResponse<>(Constants.FAILURE, 400,
                    "Something went wrong: " + ex.getMessage(), null);
        }
    }
    
    @Override
    public ApiResponse<?> getProfile(String bearerToken) {
        try {
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                System.out.println("3");
                log.warn("Invalid token header received: {}", bearerToken);
                return new ApiResponse<>(Constants.FAILURE,400,"Invalid token header",null);
            }
            String token = bearerToken.replace("Bearer ", "").trim();
            log.debug("Token extracted from header: {}", token);
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
           return new ApiResponse<>(Constants.SUCCESS,200,"Data fetched Successfully",user);

        } catch (Exception e) {
            System.out.println(7);
            return new ApiResponse<>(Constants.FAILURE,400,"Something went wrong: " + e.getMessage(), null);
        }
    }
    
    @Override
    public ApiResponse<?> forgetPassword(ForgotPasswordRequest request) {
        try {
            String email = request.getEmail().trim().toLowerCase();
            String otp = request.getOtp();
            String newPassword = request.getNewPassword();

            Optional<EmailOtp> otpOpt = emailOtpRepository
                    .findTopByEmailAndPurposeAndStatusAndVerifiedOrderByCreatedAtDesc(email,
                            Purpose.ForgetPassword.getPurpose(),UserStatus.ACTIVE.name(),true);

            if (otpOpt.isEmpty()) {
                return new ApiResponse<>(Constants.FAILURE,400,"Invalid or expired OTP",null);
            }
            EmailOtp verifiedOtp = otpOpt.get();

            boolean otpMatches = encryptOtpEnable
                    ? passwordEncoder.matches(otp, verifiedOtp.getOtp())
                    : verifiedOtp.getOtp().equals(otp);

            if (!otpMatches) {
                return new ApiResponse<>(Constants.FAILURE,400,"Invalid OTP",null);
            }

            if (verifiedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
                return new ApiResponse<>(Constants.FAILURE,400,"OTP has expired",null);
            }
            User user = verifiedOtp.getUser();
            ApiResponse<?> response = changeUserPassword(user, newPassword, true);

            verifiedOtp.setStatus(UserStatus.EXPIRED.name());
            emailOtpRepository.save(verifiedOtp);

            return response;
        } catch (Exception e) {
            log.error("Reset password failed", e);
            return new ApiResponse<>(Constants.FAILURE, 500, "Something went wrong", null);
        }
    }

    private ApiResponse<?> changeUserPassword(User user, String newPassword, boolean invalidateAllSessions) {
        log.info("New Password in ChangeUserPassword() : {}", newPassword);

        try {
            if (newPassword == null || newPassword.trim().length() < 8) {
                return new ApiResponse<>(Constants.FAILURE, 400, "Password must be at least 8 characters", null);
            }
            String encodedPassword = newPassword;
            if (encryptPasswordEnable) {
                encodedPassword = passwordEncoder.encode(newPassword);
                log.info("Password encrypted (BCrypt) for user: {}", user.getEmail());
            } else {
                log.warn("Password encryption DISABLED - saving plain text for: {}", user.getEmail());
            }
            user.setPassword(encodedPassword);
            userRepository.save(user);
            log.info("Password updated successfully for user: {}", user.getEmail());

            return new ApiResponse<>(Constants.SUCCESS,200,"Password changed successfully",null);

        } catch (Exception e) {
            log.error("Password change failed for user: {}", user.getEmail(), e);
            return new ApiResponse<>(Constants.FAILURE,500,"Failed to change password",null);
        }
    }
    
    @Override
    public ApiResponse<?> updateProfilePic(String authHeader, ProfilePicRequest request) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ApiResponse<>(Constants.FAILURE,400,"Invalid Authorization header",null);
            }

            String token = authHeader.substring(7);
            log.info("Token : {}", token);

            String email = jwtUtil.extractEmail(token);
            log.info("Email from Token : {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new FileStorageException("User not found"));
            log.info("User from UserRepo : {}", user);

            MultipartFile file = request.getProfilePic();
            log.info("Image from Request : {}", file);

            if (file == null || file.isEmpty()) {
                log.info("File is empty : {}", file);
                return new ApiResponse<>(Constants.FAILURE,400,"Profile picture file is missing",null);
            }

            String uploadedFileName = uploadingImage.uploadProfilePic(user, file);
            log.info("After image upload to db : {}", uploadedFileName);

            log.error("Profile picture updated successfully...");
            return new ApiResponse<>(Constants.SUCCESS,200,"Profile picture updated successfully",uploadedFileName);

        } catch (FileStorageException e) {
            log.error("Profile picture update failed: {}", e.getMessage());
            return new ApiResponse<>(Constants.FAILURE,400,e.getMessage(),null);

        } catch (Exception e) {
            log.error("Unexpected error while updating profile picture", e);
            return new ApiResponse<>(Constants.FAILURE,500,"Something went wrong while updating profile picture",null);
        }
    }
    
//    @Override
//    public ApiResponse<byte[]> getProfilePic(String bearerToken) {
//        try {
//            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
//                return new ApiResponse<>("FAILURE", 400, "Invalid Authorization header", null);
//            }
//
//            String token = bearerToken.substring(7);
//            String email = jwtUtil.extractEmail(token);
//
//            Optional<User> userOpt = userRepository.findByEmail(email);
//            if (userOpt.isEmpty()) {
//                return new ApiResponse<>("FAILURE", 404, "User not found", null);
//            }
//
//            User user = userOpt.get();
//
//            if (user.getProfilePic() == null || user.getProfilePic().isEmpty()) {
//                return new ApiResponse<>("FAILURE", 404, "Profile picture not found", null);
//            }
//
//            String filename = user.getProfilePic();
//            if (filename.contains("resources/")) {
//                filename = filename.substring(filename.indexOf("resources/") + "resources/".length());
//            }
//
//            log.info("Path after resources folder: {}", filename);
//
//            ClassPathResource imgFile = new ClassPathResource(filename);
//            if (!imgFile.exists()) {
//                return new ApiResponse<>("FAILURE", 404, "Profile image file not found on server", null);
//            }
//
//            try (InputStream in = imgFile.getInputStream()) {
//                byte[] imageBytes = in.readAllBytes();
//                return new ApiResponse<>("SUCCESS", 200, "Profile picture fetched successfully", imageBytes);
//            }
//
//        } catch (Exception e) {
//            log.error("Error fetching profile picture: {}", e.getMessage(), e);
//            return new ApiResponse<>("FAILURE", 500, "Something went wrong while fetching profile picture", null);
//        }
//    }
    
    @Override
    public ApiResponse<byte[]> getProfilePic(String bearerToken) {

        try {
            String token = bearerToken.substring(7);
            String email = jwtUtil.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String fileName = user.getProfilePic();

            if (fileName == null) {
                return new ApiResponse<>("FAILURE", 404, "Profile pic not found", null);
            }

            Path filePath = Paths.get(uploadDir)
                    .resolve(fileName)
                    .normalize()
                    .toAbsolutePath();

            log.info("Reading image from path: {}", filePath);

            if (!Files.exists(filePath)) {
                return new ApiResponse<>("FAILURE", 404, "Image file not found", null);
            }

            byte[] imageBytes = Files.readAllBytes(filePath);

            return new ApiResponse<>("SUCCESS", 200,
                    "Profile pic fetched successfully",
                    imageBytes);

        } catch (Exception e) {
            log.error("Error fetching profile picture", e);
            return new ApiResponse<>("FAILURE", 500,
                    "Error fetching image",
                    null);
        }
    }
    

//    @Override
//    public ApiResponse<byte[]> getProfilePic(String bearerToken) {
//
//        try {
//            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
//                return new ApiResponse<>("FAILURE", 400, "Invalid token", null);
//            }
//
//            String token = bearerToken.substring(7);
//            String email = jwtUtil.extractEmail(token);
//
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            if (user.getProfilePic() == null) {
//                return new ApiResponse<>("FAILURE", 404, "Profile pic not found", null);
//            }
//
//            Path filePath = Paths.get(uploadDir).resolve(user.getProfilePic()).normalize();
//
//            log.info("Final File Path: {}", filePath.toAbsolutePath());
//
//            if (!Files.exists(filePath)) {
//                return new ApiResponse<>("FAILURE", 404, "Image file not found", null);
//            }
//
//            byte[] imageBytes = Files.readAllBytes(filePath);
//
//            return new ApiResponse<>("SUCCESS", 200, "Profile pic fetched", imageBytes);
//
//        } catch (Exception e) {
//            log.error("Error fetching profile pic", e);
//            return new ApiResponse<>("FAILURE", 500, "Error fetching image", null);
//        }
//    }
    
    @Override
    public ApiResponse<?> filterProfiles(String token, FilterProfileRequest request) {
        log.info("Filter Request Received: {}", request);

        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ApiResponse<>("FAILURE", 400, "Invalid Token", null);
            }

            token = token.replace("Bearer ", "").trim();
            String email = jwtUtil.extractEmail(token);

            log.info("Extracted Email From Token: {}", email);

            User loggedInUser = userRepository.findByEmail(email)
                    .orElse(null);

            if (loggedInUser == null) {
                return new ApiResponse<>("FAILURE", 404, "User not found!", null);
            }

            log.info("User Verified For Filter Request: {}", loggedInUser.getId());

            // ⭐ 3) Dynamic Filtering (Only non-empty fields)
            List<User> filteredUsers = userRepository.findAll((root, query, cb) -> {
                Predicate predicate = cb.conjunction();

                if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("firstName"), request.getFirstName()));
                }
                if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("lastName"), request.getLastName()));
                }
                if (request.getProfession() != null && !request.getProfession().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("profession"), request.getProfession()));
                }
                if (request.getCategory() != null && !request.getCategory().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("category"), request.getCategory()));
                }
                if (request.getCity() != null && !request.getCity().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("city"), request.getCity()));
                }
                if (request.getState() != null && !request.getState().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("state"), request.getState()));
                }
                 return predicate;
            });

            log.info("Filtered User Count: {}", filteredUsers.size());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userId", loggedInUser.getId());
            responseData.put("profiles", filteredUsers);
            return new ApiResponse<>("SUCCESS",200,"Profiles fetched successfully",responseData);

        } catch (Exception e) {
            log.error("Error Filtering Profiles: ", e);
            return new ApiResponse<>( "FAILURE",500,"Something Went Wrong...",null);
            
            
        }
    }
    
    private void handleLoginFailed(User user) {
		if(ObjectUtils.isEmpty(user.getLoginFailedRetries())) {
			user.setLoginFailedRetries(0);
			user.setStatusMessage("");
		}
		user.setLoginFailedRetries(user.getLoginFailedRetries()+1);
		if(user.getLoginFailedRetries() >= Constants.LOGIN_RETRIES_ALLOWED) {
			user.setStatus(UserStatus.VERIFICATION_PENDING.name());
			user.setStatusMessage(Constants.LOGIN_RETRIES_EXAUST_USER_STATUS_MESSAGE);
		}
		userRepository.save(user);
	}
}

  

