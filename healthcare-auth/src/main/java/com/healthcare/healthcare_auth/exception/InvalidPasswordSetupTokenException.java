package com.healthcare.healthcare_auth.exception;

public class InvalidPasswordSetupTokenException extends RuntimeException {
    public InvalidPasswordSetupTokenException(String message) {
        super(message);
    }
}
