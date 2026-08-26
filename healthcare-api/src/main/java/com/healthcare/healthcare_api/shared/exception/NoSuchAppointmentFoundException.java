package com.healthcare.healthcare_api.shared.exception;

public class NoSuchAppointmentFoundException extends RuntimeException {
    public NoSuchAppointmentFoundException(String message) {
        super(message);
    }
}
