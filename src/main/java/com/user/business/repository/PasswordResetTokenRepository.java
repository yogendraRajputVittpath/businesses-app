package com.user.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.business.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> { }
