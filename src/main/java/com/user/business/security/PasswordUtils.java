package com.user.business.security;

public class PasswordUtils {

    // Regex for BCrypt hash
    private static final String BCRYPT_PATTERN = "^\\$2[aby]?\\$\\d{2}\\$[./A-Za-z0-9]{53}$";

    /**
     * Checks if the given string is a BCrypt hash.
     *
     * @param password the password or hash
     * @return true if it's a BCrypt hash, false if it's a plain string
     */
    public static boolean isBCryptHash(String password) {
        if (password == null) {
            return false;
        }
        return password.matches(BCRYPT_PATTERN);
    }

    public static void main(String[] args) {
        String plain = "mySimplePassword";
        String bcrypt = "$2a$10$Bq0u9y2tbYkJyslipWg1Qu7oPiUGkRk3L5WwM6sRcUGU0bO2i8FJ6";

        System.out.println("Plain: " + isBCryptHash(plain));   // false
//        System.out.println("BCrypt: " + isBCryptHash(bcrypt)); // true
    }
}
