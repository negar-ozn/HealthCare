package com.healthcare.healthcare_api.department;

import lombok.Builder;

import java.time.Instant;

@Builder
public record DepartmentResponse(

        String name,
        String message

) {
}
