
package com.user.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.user.business.entity.UserToken;

import jakarta.transaction.Transactional;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    
	Optional<UserToken> findByToken(String token);
    
    @Modifying
    @Transactional
    void deleteByToken(String token);
}
