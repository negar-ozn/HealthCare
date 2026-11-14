package com.healthcare.healthcare_api.shared.exception;

public class PersonnelNotFoundException extends RuntimeException {
    public PersonnelNotFoundException(String message) {
        super(message);
    }
}
