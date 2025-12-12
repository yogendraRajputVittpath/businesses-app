
package com.user.business.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Primary Key (har OTP record ke liye unique)

    // 🔹 Foreign Key mapping with User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false) // user_id column banega jo users(id) ko refer karega
    private User user;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String otp;

    @Column(nullable = false, length = 50)  // e.g. REGISTER, RESET_PASSWORD, AccountVerification
    private String purpose;
    
//    private UserStatus status;
    private String status = "Failure";
    
 // ✅ verified column (bit(1)) ko Boolean map karte hain
    @Column(nullable = false)
    private Boolean verified = false;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
  
    @PrePersist
    public void prePersist() {
        if (verified == null) {
            verified = false;
        }
    }
}
