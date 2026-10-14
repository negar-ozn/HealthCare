package com.healthcare.healthcare_api.doctor;

import com.healthcare.healthcare_api.department.Department;
import com.healthcare.healthcare_api.personnel.Personnel;
import com.healthcare.healthcare_api.shared.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Builder
public record DoctorRequest(

        @NotBlank String specialization,
        @NotNull Long departmentId,
        @NotNull Long licenceNumber,
        @NotNull Status status,
        @NotNull Long personnelId
) {
}
