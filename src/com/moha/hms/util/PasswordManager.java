package com.moha.hms.util;

import org.mindrot.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordManager {
    public static String encryptPassword(String plaintText) {
        return BCrypt.hashpw(plaintText,BCrypt.gensalt(10));
    }

    public static boolean checkPassword(String plainText, String hash){
        return BCrypt.checkpw(plainText, hash);
    }

    public static String getPasswordCriteria(String password) {
        StringBuilder missingCriteria = new StringBuilder();

        // Minimum length of 8 characters
        if (password.length() < 8) {
            missingCriteria.append("Minimum length of 8 characters.\n");
        }

        // Contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            missingCriteria.append("Contains at least one uppercase letter.\n");
        }

        // Contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            missingCriteria.append("Contains at least one lowercase letter.\n");
        }

        // Contains at least one digit
        if (!password.matches(".*\\d.*")) {
            missingCriteria.append("Contains at least one digit.\n");
        }

        // Contains at least one special character from a predefined set
        Pattern specialChars = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");
        Matcher matcher = specialChars.matcher(password);
        if (!matcher.find()) {
            missingCriteria.append("Contains at least one special character.\n");
        }

        return missingCriteria.toString();
    }

}
