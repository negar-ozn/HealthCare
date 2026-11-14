package com.healthcare.healthcare_auth.util;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

public class SecretGenerator {

    public static void main(String[] args) {

        SecretKey key = Jwts.SIG.HS256.key().build();
        String secret = io.jsonwebtoken.io.Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secret);
    }
}


