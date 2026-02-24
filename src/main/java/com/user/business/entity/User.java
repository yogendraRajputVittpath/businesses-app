package com.user.business.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private string

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    @JsonIgnore
//    @Column(nullable = false)
    private String password;

    private LocalDate dob;

    @Column(length = 15, nullable = false, unique = true)
    private Long phoneNo;

    private String alternatePhone;

    // Address
    private String address;
    private String city;
    private String state;

    @Column(length = 6)
    private String pincode;

    private String businessName;
    private String userName;
    private String website;

    private LocalDate registeredDate;     
    private String registeredAddress;    

    private String logo;                 

    private BigDecimal turnover;         

    private String industry;            

    private String cin;                   
    private String gst;                   

    // Other details
    private String highestEducation;

    private BigDecimal monthlyEarning;

    @Column(length = 12, unique = true)
    private String aadharCardNo;

    @Column(length = 10, unique = true)
    private String panCardNo;

    private LocalDate createdDate;

    private LocalDateTime lastLogin;

    private String profession;

    private String category;

    private String profilePic;

    @Column(nullable = false)
    private String status;  // VerificationPending / Verified / Active / Disabled
    
    @Column(length = 255)
    private String statusMessage;

//    @Column(nullable = false)
    @Column(nullable = true)
    private String gender; // MALE/FEMALE/OTHER
    
    private Integer loginFailedRetries = 0;
    
    private boolean emailNotificationEnabled = true;
}

