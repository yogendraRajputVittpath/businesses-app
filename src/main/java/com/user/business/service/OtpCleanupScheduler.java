package com.user.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.user.business.repository.EmailOtpRepository;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OtpCleanupScheduler {

    private final EmailOtpRepository emailOtpRepository;

    @Scheduled(cron = "${cleanup.otp.cron}") // 11:50 PM daily
    public void deleteOldOtps() {

        log.info("Time to delete otp...");
        
        LocalDate yesterday = LocalDate.now().minusDays(1);

        emailOtpRepository.deleteYesterdayOtps(yesterday);

        log.info("Yesterday’s OTPs deleted successfully — Today's OTPs are safe.");
    }

}
