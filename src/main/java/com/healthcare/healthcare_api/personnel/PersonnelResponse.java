package com.healthcare.healthcare_api.personnel;

import com.healthcare.healthcare_api.department.Department;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record PersonnelResponse(

        String firstName,
        String lastName,
        LocalDate birthdate,
        String gender,
        Long nationalId,
        String phone,
        String email,
        String address,
        String role,
        String personnelCode,
        Department department,
        Instant createdAt,
        Instant updatedAt
) {
}
