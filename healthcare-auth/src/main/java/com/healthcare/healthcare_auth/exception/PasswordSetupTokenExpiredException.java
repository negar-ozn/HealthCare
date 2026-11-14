package com.healthcare.healthcare_auth.exception;

public class PasswordSetupTokenExpiredException extends RuntimeException {
    public PasswordSetupTokenExpiredException(String message) {
        super(message);
    }
}
