package com.healthcare.healthcare_api.department;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Builder
public record DepartmentRequest(

        Long id,
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NonNull
        Instant createdAt,
        @NonNull
        Instant updatedAt
) {
}
