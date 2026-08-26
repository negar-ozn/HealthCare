package com.healthcare.healthcare_api.schedule;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.enums.Weekday;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ScheduleResponse(

        Doctor doctor,
        Weekday weekday,
        LocalDate date,
        LocalTime time,
        Status status,
        Instant createdAt,
        Instant updatedAt
) {
}
