package com.healthcare.healthcare.exception;

public class NoSuchPatientFoundException extends Exception {
    public NoSuchPatientFoundException(String message) {
        super(message);
    }
}
