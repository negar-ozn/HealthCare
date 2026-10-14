package com.healthcare.healthcare_api.appointment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AppointmentRequest(

        @NotNull Long scheduleId,
        @Size(max = 500) String visitReason
) {
}
