package com.healthcare.healthcare_audit.kafka.event;

import lombok.Builder;

@Builder
public record AppointmentCanceledEvent(

        Long userId,
        String username,
        String action,
        String entityName,
        String status,
        String details
) {
}
