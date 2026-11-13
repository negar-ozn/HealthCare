package com.healthcare.healthcare_api.department;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record DepartmentResponse(

        String name,
        String message
) implements Serializable {
}
