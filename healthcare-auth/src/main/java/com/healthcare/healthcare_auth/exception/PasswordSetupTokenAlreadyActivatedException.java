package com.healthcare.healthcare_auth.exception;

public class PasswordSetupTokenAlreadyActivatedException extends RuntimeException {
    public PasswordSetupTokenAlreadyActivatedException(String message) {
        super(message);
    }
}
