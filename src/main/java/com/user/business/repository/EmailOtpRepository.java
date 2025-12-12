package com.user.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.user.business.entity.EmailOtp;
import com.user.business.entity.UserStatus;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {

    // Find latest OTP for a given email and purpose
    Optional<EmailOtp> findTopByEmailAndPurposeOrderByCreatedAtDesc(
    		String email,
    		String purpose);

    // Find specific OTP record
    Optional<EmailOtp> findTopByEmailAndOtpAndPurposeOrderByCreatedAtDesc(
            String email, 
            String otp, 
            String purpose);

//	Optional<EmailOtp> findValidOtp(Long userId, String email, String purpose, String otp, LocalDateTime now);
    Optional<EmailOtp> findByUserIdAndEmailAndOtpAndPurposeAndExpiryTimeAfter(
            Long userId,
            String email,
            String otp,
            String purpose,
            LocalDateTime now);

	Optional<EmailOtp> findTopByUserIdAndEmailAndPurposeOrderByCreatedAtDesc(
			Long userId,
			String email,
			String purpose);

	void deleteByCreatedAtBefore(
			LocalDateTime cutoffTime);
	
	@Modifying
	@Transactional
	@Query("UPDATE EmailOtp e SET e.otp = NULL WHERE e.email = :email AND e.otp = :otp")
	void deleteOnlyOtp(String email, String otp);
	
	@Modifying
	@Transactional
	@Query("UPDATE EmailOtp e SET e.otp = NULL WHERE e.createdAt < :expiryTime")
	void deleteExpiredOtps(LocalDateTime expiryTime);


//	Optional<EmailOtp> findTopByUserIdAndEmailAndPurposeAndStatusOrderByCreatedAtDesc(
	Optional<EmailOtp> findTopByEmailAndPurposeAndStatusOrderByCreatedAtDesc(
//			Long userId,
			String email,
			String purpose,
			UserStatus status);

//	Optional<EmailOtp> findTopByUserIdAndEmailAndPurposeAndStatusOrderByCreatedAtDesc(
	Optional<EmailOtp> findTopByEmailAndPurposeAndStatusOrderByCreatedAtDesc(
//			Long userId,
			String email,
			String purpose,
			String name);

	Optional<EmailOtp> findTopByEmailAndPurposeAndStatusAndVerifiedOrderByCreatedAtDesc(
			String email,
			String purpose,
			String name, 
			boolean b);
	
//	@Modifying
//	@Transactional
//	@Query("DELETE FROM EmailOtp e WHERE DATE(e.createdAt) = :yesterday")
//	void deleteYesterdayOtps(LocalDate yesterday);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM EmailOtp e WHERE FUNCTION('DATE', e.createdAt) = :yesterday")
	void deleteYesterdayOtps(LocalDate yesterday);



}			

