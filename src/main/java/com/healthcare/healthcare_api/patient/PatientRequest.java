package com.healthcare.healthcare_api.patient;

import com.healthcare.healthcare_api.insurance.Insurance;
import com.healthcare.healthcare_api.shared.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record PatientRequest (

    Long id,
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @NotNull
    LocalDate birthdate,
    @NotBlank
    String gender,
    @Pattern(regexp = "\\d{10}")
    Long nationalId,
    @NotBlank
    String phone,
    @Email
    String email,
    @NotBlank
    String address,
    @NotNull
    Boolean arrival,
    Insurance insurance,
    @NotBlank
    Status status,
    @NotBlank
    String patientNumber,
    @NotNull
    Instant createdAt,
    @NotNull
    Instant updatedAt
) {}
