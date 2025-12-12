//package com.papertrading.service;
//
//import jakarta.mail.*;
//import jakarta.mail.internet.*;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Properties;
//
//@Slf4j
//public class EmailService {
//
//	//private static boolean flag = false;
////    public static void main(String[] args) {
//	public static boolean sendEmailOtp(String to, String otp) {
//
////		String otp = OTPGenerator.generateOtp();
//		
//        // Sender's email credentials
//        final String username = "info@vittpath.com";
//        final String password = "@1234Asdf@"; // Use App Password ideally
//
//        // Recipient's email
////        String toEmail = "snehachauhan635@gmail.com";  // Replace with actual recipient
//        String toEmail = to;
//        
//        // SMTP server configuration
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
////        props.put("mail.smtp.starttls.enable", "true"); // TLS
//        props.put("mail.smtp.ssl.enable", "true");
////        props.put("mail.smtp.host", "smtp.vittpath.com"); // Replace with actual SMTP server
//        props.put("mail.smtp.host", "smtp.hostinger.com");
//        props.put("mail.smtp.port", "465"); // TLS port (or use 465 for SSL)
//
//        // Create a session with authentication
//        Session session = Session.getInstance(props, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            // Create the email message
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse(toEmail)
//            );
//
//            message.setSubject("OTP");
////            message.setText("Your OTP : "+OTPGenerator.generateOtp());
//            message.setText("Your OTP : "+otp);
//            
//            // Send the message
//            Transport.send(message);
//
//        } catch (MessagingException e) {
//        	log.error("Unable to Send Email : ",e);
//            return false;
//        }
//        log.info("Email sent successfully!");
//        
//        return true;
//    }
//}

package com.user.business.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender; // Spring inject karega automatically
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public boolean sendEmailOtp(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(to);
            helper.setFrom(fromEmail);
            helper.setSubject("OTP Verification");
            helper.setText("Your OTP is: " + otp);
            mailSender.send(message);
            log.info("OTP email sent successfully to {}", to);
            return true;
        } catch (MessagingException e) {
            log.error("Failed to send OTP email", e);
            return false;
        }
    }
}
