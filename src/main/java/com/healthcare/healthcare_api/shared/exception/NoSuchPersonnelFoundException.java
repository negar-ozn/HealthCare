package com.healthcare.healthcare_api.shared.exception;

public class NoSuchPersonnelFoundException extends RuntimeException {
    public NoSuchPersonnelFoundException(String message) {
        super(message);
    }
}
