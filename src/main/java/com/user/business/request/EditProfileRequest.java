//package com.user.business.request;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import jakarta.validation.constraints.*;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Data
//public class EditProfileRequest {
//
//    // First Name (5–50 letters only)
//    @Pattern(regexp = "^[A-Za-z]{5,50}$", message = "First Name must contain only letters (5–50 chars)")
//    private String firstName;
//
//    // Last Name (5–50 letters only)
//    @Pattern(regexp = "^[A-Za-z]{5,50}$", message = "Last Name must contain only letters (5–50 chars)")
//    private String lastName;
//
//    // ⭐ Mother Name (5–50 letters)
//    @Pattern(regexp = "^[A-Za-z]{5,50}$", message = "Mother Name must contain only letters (5–50 chars)")
//    private String motherName;
//
//    // ⭐ Father Name (5–50 letters)
//    @Pattern(regexp = "^[A-Za-z]{5,50}$", message = "Father Name must contain only letters (5–50 chars)")
//    private String fatherName;
//
//    // DOB - must be past date
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
//    private LocalDate dob;
//
//    // Alternate Phone (optional)
//    @Pattern(regexp = "^[0-9]{10,13}$", message = "Alternate phone must be 10–13 digits")
//    private String alternatePhone;
//
//    // Profile Picture URL
//    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid Profile Pic URL")
//    private String profilePic;
//
//    // Highest Education
//    private String highestEducation;
//
//    // Monthly Earning >= 0
//    @DecimalMin(value = "0.0", message = "Monthly earning must be >= 0")
//    private BigDecimal monthlyEarning;
//
//    // Aadhaar (12 digits)
//    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhar number must be 12 digits")
//    private String aadharCardNo;
//
//    // PAN (ABCDE1234F)
//    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN format")
//    private String panCardNo;
//
//    // Profession
//    private String profession;
//
//    // Gender
//    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Gender must be MALE/FEMALE/OTHER")
//    private String gender;
//
//    // Category
//    private String category;
//
//    // Website
//    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid website URL")
//    private String website;
//
//    // Address details
//    private String address;
//    private String city;
//    private String state;
//
//    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid Pincode")
//    private String pincode;
//}
//
//

package com.user.business.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EditProfileRequest {

    @NotBlank(message = "Business Name is required")
    @Pattern(regexp = "^[A-Za-z ]{5,50}$", message = "Business Name must be 5–50 letters")
    private String businessName;

    @Pattern(regexp = "^[A-Za-z]{5,50}$", message = "User Name must be 5–50 letters")
    private String userName;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid Website URL")
    private String website;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate registeredDate;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private long phoneNo;

    @Pattern(regexp = "^[0-9]{10,13}$", message = "Alternate phone must be 10–13 digits")
    private String alternatePhone;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid Logo URL")
    private String logo;

    @DecimalMin(value = "0.0", message = "Turnover must be >= 0")
    private BigDecimal turnover;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN format")
    private String panCardNo;

    private String industry; // Medical / Finance / Education / IT / Machinery / Furniture / Hotel

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "^(VerificationPending|Verified|Active|Disabled)$",
            message = "Invalid status"
    )
    private String status;

    private String registeredAddress;
    private String city;
    private String state;

    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid Pincode")
    private String pincode;

    @Pattern(regexp = "^[0-9]+$", message = "CIN must contain only digits")
    private String cin;

    @Pattern(regexp = "^[0-9]+$", message = "GST must contain only digits")
    private String gst;
}
