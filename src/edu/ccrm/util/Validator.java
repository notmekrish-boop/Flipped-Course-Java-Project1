// Validator.java
package edu.ccrm.util;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidStudentId(String id) {
        return id != null && id.matches("S\\d{3}");
    }
    
    public static boolean isValidCourseCode(String code) {
        return code != null && code.matches("[A-Z]{2,4}\\d{3}");
    }
}
