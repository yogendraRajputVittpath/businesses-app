package com.user.business.repository.socialmedia;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.business.entity.socialmedia.SocialMedia;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Integer> {
	
	 boolean existsByUserIdAndAccountIgnoreCaseAndStatus(Integer userId,String account,String status);

    boolean existsByUserIdAndAccountIgnoreCase(Integer userId, String account);

    List<SocialMedia> findByUserId(Long userId);
}
