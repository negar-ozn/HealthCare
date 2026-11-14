package com.healthcare.healthcare_api.schedule;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.shared.enums.ScheduleStatus;
import com.healthcare.healthcare_api.shared.enums.Weekday;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ScheduleRequest(

        Long id,
        Doctor doctor,
        @NonNull Weekday weekday,
        @NonNull LocalDate date,
        @NonNull LocalTime startTime,
        @NonNull LocalTime endTime,
        @NonNull ScheduleStatus status,
        @NonNull Instant createdAt,
        @NonNull Instant updatedAt
) {
}
