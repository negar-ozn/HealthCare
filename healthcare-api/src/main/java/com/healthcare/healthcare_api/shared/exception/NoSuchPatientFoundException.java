package com.healthcare.healthcare_api.shared.exception;

public class NoSuchPatientFoundException extends RuntimeException {
    public NoSuchPatientFoundException(String message) {
        super(message);
    }
}
