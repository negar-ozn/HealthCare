package com.healthcare.healthcare_api.appointment;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.shared.enums.AppointmentStatus;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AppointmentRequest (

        Long id,
        @NotNull
        LocalDate appointmentDate,
        @NotNull
        LocalTime appointmentTime,
        @NotBlank
        AppointmentStatus status,
        @NotBlank
        String visitReason,
        @NotBlank
        String cancellationReason,
        @NotNull
        Boolean rescheduled,
        Patient patient,
        Doctor doctor,
        @NotBlank
        String appointmentNumber,
        @NotNull
        Instant createdAt,
        @NotNull
        Instant updatedAt
) {}
