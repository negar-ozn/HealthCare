package com.healthcare.healthcare_api.department;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateDepartmentRequest(

        @NotBlank String name,
        @NotBlank String description
) {
}
