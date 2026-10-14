package com.healthcare.healthcare_api.shared.exception;

public class BookedAppointmentNotFoundException extends RuntimeException {
    public BookedAppointmentNotFoundException(String message) {
        super(message);
    }
}
