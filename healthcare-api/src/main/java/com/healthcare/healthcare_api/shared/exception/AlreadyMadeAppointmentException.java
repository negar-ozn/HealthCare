package com.healthcare.healthcare_api.shared.exception;

public class AlreadyMadeAppointmentException extends RuntimeException {
    public AlreadyMadeAppointmentException(String message) {
        super(message);
    }
}
