package com.healthcare.healthcare_api.shared.exception;

public class AppointmentSlotAlreadyBookedException extends RuntimeException {
    public AppointmentSlotAlreadyBookedException(String message) {
        super(message);
    }
}
