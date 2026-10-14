package com.healthcare.healthcare_api.doctor;

import com.healthcare.healthcare_api.department.Department;
import com.healthcare.healthcare_api.personnel.Personnel;
import com.healthcare.healthcare_api.shared.enums.Status;
import lombok.Builder;

import java.time.Instant;

@Builder
public record DoctorResponse(

        String specialization,
        Department department,
        Long licenceNumber,
        Status status,
        Personnel personnel
) {
}
