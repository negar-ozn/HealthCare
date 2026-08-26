package com.healthcare.healthcare_api.shared.exception;

public class PersonnelAlreadyExistException extends RuntimeException {
    public PersonnelAlreadyExistException(String message) {
        super(message);
    }
}
