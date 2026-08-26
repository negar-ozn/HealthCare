package com.healthcare.healthcare_api.personnel;

import com.healthcare.healthcare_api.department.Department;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record PersonnelRequest (

    Long id,
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @NonNull
    LocalDate birthdate,
    @NotBlank
    String gender,
    @NonNull
    Long nationalId,
    @NotBlank
    String phone,
    @NotBlank
    String email,
    @NotBlank
    String address,
    @NotBlank
    String role,
    @NotBlank
    String personnelCode,
    @NotBlank
    Department department,
    @NonNull
    Instant createdAt,
    @NonNull
    Instant updatedAt
){}
