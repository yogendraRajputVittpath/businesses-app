//package com.user.business.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.*;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//@Entity
//@Table(name = "users")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long userId;
//    private Long Id;
//
//    @Column(length = 50, nullable = false)
//    private String firstName;
//
//    @Column(length = 50)
//    private String lastName;
//
//    @Column(length = 100, nullable = false, unique = true)
//    private String email;
//
//    @Column(length = 50)
//    private String motherName;
//
//    @Column(length = 50)
//    private String fatherName;
//
//    @Column(nullable = false)
//    @JsonIgnore
//    private String password;
//
//    private LocalDate dob;
//
//    @Column(length = 15, nullable = false, unique = true)
//    private Long phoneNo;
//
//    @Column(length = 15)
//    private String alternatePhone;
//
//    private String profilePic;
//
//    private String highestEducation;
//
//    @DecimalMin(value = "0.0")
//    private BigDecimal monthlyEarning;
//
//    @Column(length = 12, unique = true)
//    private String aadharCardNo;
//
//    @Column(length = 10, unique = true)
//    private String panCardNo;
//
//    private LocalDate createdDate;
//
//    private LocalDateTime lastLogin;
//
//    private String profession;
//
//    @Column(nullable = false)
//    private String status; // VerificationPending / Verified / Active / Disabled
//
//    @Column(nullable = false)
//    private String gender; // MALE/FEMALE/OTHER
//
//    private String category;
//
//    private String website;
//
//    private String address;
//
//    private String city;
//
//    private String state;
//
//    @Column(length = 6)
//    private String pincode;
//
//}
//

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

    // Personal details
    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
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


    // --------------------------
    // ⭐ Added Business Fields
    // --------------------------
    private String businessName;
    private String userName;
    private String website;

    private LocalDate registeredDate;     // ✔ added
    private String registeredAddress;     // ✔ added

    private String logo;                  // ✔ added

    private BigDecimal turnover;          // ✔ added

    private String industry;              // ✔ added

    private String cin;                   // ✔ added
    private String gst;                   // ✔ added


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


    // ⭐ Status should be string or Enum (your choice)
    @Column(nullable = false)
    private String status;  // VerificationPending / Verified / Active / Disabled

    @Column(nullable = false)
    private String gender; // MALE/FEMALE/OTHER
}

