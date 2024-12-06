package com.example.demo.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    // 이메일 검증
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // 비밀번호 검증
    public static boolean isValidPassword(String password) {
        // 비밀번호는 최소 8자, 특수문자 포함
        return password.length() >= 8 && password.matches(".*[!@#$%^&*()].*");
    }
}
