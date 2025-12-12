package com.user.business.token.cleanup;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.user.business.repository.UserTokenRepository;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class TokenCleanupTask {

    private final UserTokenRepository userTokenRepository;

    @Scheduled(fixedRate = 3600000) // har 1 ghante
    public void cleanExpiredTokens() {
        userTokenRepository.findAll().forEach(token -> {
            if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
                userTokenRepository.delete(token);
            }
        });
    }
}
