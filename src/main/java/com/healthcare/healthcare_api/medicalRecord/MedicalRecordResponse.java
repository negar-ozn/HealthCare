package com.healthcare.healthcare_api.medicalRecord;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record MedicalRecordResponse(

        Patient patient,
        Doctor doctor,
        LocalDate visitDate,
        String symptoms,
        String notes,
        String prescriptions,
        String prescriptionsNumber,
        Instant createdAt,
        Instant updatedAt
) {
}
