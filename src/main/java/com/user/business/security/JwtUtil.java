package com.user.business.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.user.business.exception.TokenExpiredException;
import com.user.business.response.ApiResponse;
import com.user.business.service.util.Constants;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    // Static secret key, same for all requests
    private static final String SECRET = "snehachauhanSuperSecretKeyForJwt12345"; // min 32 chars for HS256

    @Value("${token.expiry.time}")
    private String expiryTimeProperty;

    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

//    public String generateToken(String email, Long userId) {
//    	log.info("Token Generating...");
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("userId", userId) // custom claim
//                .setIssuedAt(new Date())
//                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
    
    public String generateToken(String email, Long userId) {
        log.info("Token Generating...");

        // Current time
        LocalDateTime now = LocalDateTime.now();

        // Default expiry today at 11:50 PM
        //LocalDateTime expiryTime = now.toLocalDate().atTime(23, 50);
        
        // Example: "23:50" → split into hour & minute
        String[] timeParts = expiryTimeProperty.split(":");
        int expiryHour = Integer.parseInt(timeParts[0]);
        int expiryMinute = Integer.parseInt(timeParts[1]);
        
        // Default expiry based on property time
        LocalDateTime expiryTime = now.toLocalDate().atTime(expiryHour, expiryMinute);

        // If current time already passed 11:50 PM -> set expiry next day at 11:50 PM
        if (now.isAfter(expiryTime)) {
            expiryTime = now.plusDays(1).toLocalDate().atTime(23, 50);
        }

        Date expiryDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)  // <-- NEW EXPIRY
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


//    public ApiResponse<?> validateToken(String token) {
    public boolean validateToken(String token) {
    	try {
        	log.info("Check Token Validation...");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
        	log.error("Token Expire...");
        	throw new TokenExpiredException("Your Session has expired, please login again.");
        }catch (Exception e) {
        	log.error("Token Expired by time out...");
        	return false; // ❌ Invalid token
		}
    }

    public String extractEmail(String token) {
    	log.info("Extrect Email from Token...");
    	
        return Jwts.parserBuilder()
        		.setSigningKey(key)
        		.build()
        		.parseClaimsJws(token)
        		.getBody()
        		.getSubject();
    }
    
 //  Extract userId from claims
    public Long extractUserId(String token) {
    	log.info("Extrect UserId from Token...");
    	
    	String trimToken = token.replace("Bearer ", "").trim();
        log.info("Token extracted from header: {}", trimToken);
    	
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
//                .parseClaimsJws(token)
                .parseClaimsJws(trimToken)
                .getBody();
        
        log.info(claims.toString());
        return claims.get("userId", Long.class);
    }
    
 // Check if expired
    public boolean isTokenExpired(String token) {
    	log.info("[JWT] Checking if token is expired for token: {}", token);
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
        	log.error("[JWT] Error while checking token expiration: {}", e.getMessage());
            return true;
        }
    }
    
 //  Added hashToken() method from TokenUtil
    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing token", e);
        }
    }
    
 }

