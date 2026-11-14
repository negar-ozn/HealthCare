package com.healthcare.healthcare_api.shared.exception;

public class RescheduledAppointmentException extends RuntimeException {
    public RescheduledAppointmentException(String message) {
        super(message);
    }
}
