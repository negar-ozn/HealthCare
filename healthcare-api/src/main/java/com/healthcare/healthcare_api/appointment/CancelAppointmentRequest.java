package com.healthcare.healthcare_api.appointment;

import jakarta.validation.constraints.Size;

public record CancelAppointmentRequest(

        @Size(max = 500)
        String cancellationReason
) {
}
