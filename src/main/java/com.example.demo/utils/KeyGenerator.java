package com.example.demo.utils;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        var key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // HS512 대신 HS256 사용 가능
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
    }
}
