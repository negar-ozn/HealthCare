package com.healthcare.healthcare_auth.exception;

public class PasswordSetupTokenAlreadyUsedException extends RuntimeException {
    public PasswordSetupTokenAlreadyUsedException(String message) {
        super(message);
    }
}
