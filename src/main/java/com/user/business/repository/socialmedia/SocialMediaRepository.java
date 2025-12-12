
package com.user.business.repository.socialmedia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.business.entity.socialmedia.SocialMedia;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Integer> {

    boolean existsByUserIdAndAccountIgnoreCase(Integer userId, String account);
//	    boolean existsByAccountIgnoreCase(String account);
//    Optional<SocialMedia> findByUserIdAndAccountIgnoreCase(Integer userId, String account);

}
