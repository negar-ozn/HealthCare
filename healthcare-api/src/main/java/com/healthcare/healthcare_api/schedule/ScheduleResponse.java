package com.healthcare.healthcare_api.schedule;

import com.healthcare.healthcare_api.shared.enums.ScheduleStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ScheduleResponse(

        Long id,
        Long doctorId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        ScheduleStatus status
) {
}
