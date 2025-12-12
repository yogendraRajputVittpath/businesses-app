
package com.user.business.entity.socialmedia;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "social_media",
       indexes = {@Index(name = "idx_user_account", columnList = "user_id, account")})
public class SocialMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false, length = 50)
    private String account;

    @Column(nullable = false, length = 500)
    private String link;

    private Long followers;

    @Column(name = "account_creation_date")
    private LocalDate accountCreationDate;

    @Column(name = "total_views")
    private Long totalViews;

    @Column(name = "total_posts")
    private Long totalPosts;

    @Column(name = "total_likes")
    private Long totalLikes;

    @Column(nullable = false, length = 20)
    private String status; // ACTIVE / REMOVED

//    @Column(name = "created_date", nullable = false)
//    private LocalDateTime createdDate;
//
//    @Column(name = "modified_date", nullable = false)
//    private LocalDateTime modifiedDate;

}

