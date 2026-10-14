package com.healthcare.healthcare_api.medicalRecord;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record MedicalRecordResponse(

        Long patientId,
        Long doctorId,
        LocalDate visitDate,
        String symptoms,
        String notes,
        String prescriptions,
        String prescriptionsNumber
) {
}
