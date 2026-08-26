package com.healthcare.healthcare_api.shared.exception;

public class NoSuchRecordFoundException extends RuntimeException {
    public NoSuchRecordFoundException(String message) {
        super(message);
    }
}
