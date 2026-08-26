package com.healthcare.healthcare_api.patient;

import com.healthcare.healthcare_api.insurance.Insurance;
import com.healthcare.healthcare_api.shared.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record PatientResponse(

        String firstName,
        String lastName,
        LocalDate birthdate,
        String gender,
        Long nationalId,
        String phone,
        String email,
        String address,
        Boolean arrival,
        Insurance insurance,
        Status status,
        String patientNumber,
        Instant createdAt,
        Instant updatedAt
) {
}
