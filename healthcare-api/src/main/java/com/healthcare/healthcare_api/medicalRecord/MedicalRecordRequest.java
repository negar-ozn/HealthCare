package com.healthcare.healthcare_api.medicalRecord;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record MedicalRecordRequest(

        Long id,
        Patient patient,
        Doctor doctor,
        @NotNull LocalDate visitDate,
        @NotBlank String symptoms,
        @NotBlank String notes,
        @NotBlank String prescriptions,
        @NotBlank String prescriptionsNumber,
        @NotNull Instant createdAt,
        @NotNull Instant updatedAt
) {}
