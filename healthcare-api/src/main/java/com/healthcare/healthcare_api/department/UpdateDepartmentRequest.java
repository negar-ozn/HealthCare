package com.healthcare.healthcare_api.department;

import lombok.*;

@Builder
public record UpdateDepartmentRequest(

        String name,
        String description
) {
}
