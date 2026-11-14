package com.healthcare.healthcare_api.appointment;

import com.healthcare.healthcare_api.shared.enums.AppointmentStatus;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = ApiDescription.APPOINTMENT_RESPONSE)
@Builder
public record AppointmentResponse(

        Long id,
        Long scheduleId,
        Long patientId,
        Long doctorId,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        AppointmentStatus status,
        String visitReason,
        String cancellationReason,
        Boolean rescheduled,
        String appointmentNumber
) {
}
