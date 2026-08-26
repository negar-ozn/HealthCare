package com.healthcare.healthcare_api.shared.exception;

public class NoBookedAppointmentFoundException extends RuntimeException {
    public NoBookedAppointmentFoundException(String message) {
        super(message);
    }
}
