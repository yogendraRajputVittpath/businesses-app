package com.user.business.service.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public class Encryptor {

    private static final Logger log = LoggerFactory.getLogger(Encryptor.class);
    // Read these values from property File
    private static String initVector = "qwertyuiasdfghjk";
    private static String secretKey = "zxcvbnmasdfghjklqwertyuiopasdfgh";

    public static void main(String[] args) throws Exception {

        String[] values = {
            "sneha@123",
            "1234",
            "@shubangi22pachuari",
            "info@vittpath.com",
            "@1234Asdf@",
            "0g5iw80s89eu3xyy",
            "g6pyh0n95bdc8a67p3kriurgd69fdbps"
        };

        List<String> list = new ArrayList<>();

        for (String val : values) {
            list.add(encrypt(val));
        }

        // Print decrypted values for verification
        System.out.println("==============================================");
        for (String val : list) {
            decrypt(val);
        }

        String s1 = decrypt("wIaZWEpzNf1oqtF4KsOrfQ==");
        System.out.println(s1);
    }

    public static String encrypt(String decryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, secKey, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(decryptedValue.getBytes(StandardCharsets.UTF_8));
            String encryptedValue = Base64.getEncoder().encodeToString(encryptedBytes);

            log.info("'{}' Encrypted To -> '{}'", decryptedValue, encryptedValue);
            return encryptedValue;
        } catch (Exception e) {
            log.error("Error encrypting value: {}", decryptedValue, e);
            return null;
        }
    }

    public static String decrypt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            cipher.init(Cipher.DECRYPT_MODE, secKey, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            String decryptedValue = new String(decryptedBytes);

            log.info("'{}' Decrypted Successfully -> {}", encryptedValue, decryptedValue);
            return decryptedValue;
        } catch (Exception e) {
            log.error("Error decrypting value: {}", encryptedValue, e);
            return null;
        }
    }
}
