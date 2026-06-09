package com.healthcare.healthcare.exception;

public class PatientAlreadyExistsException extends Exception {
    public PatientAlreadyExistsException(String message) {
        super(message);
    }
}
