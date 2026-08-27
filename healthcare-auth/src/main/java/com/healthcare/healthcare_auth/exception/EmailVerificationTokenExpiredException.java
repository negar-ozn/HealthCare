package com.healthcare.healthcare_auth.exception;

public class EmailVerificationTokenExpiredException extends RuntimeException {
    public EmailVerificationTokenExpiredException(String message) {
        super(message);
    }
}
