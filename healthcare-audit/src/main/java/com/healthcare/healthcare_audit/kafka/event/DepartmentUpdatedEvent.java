package com.healthcare.healthcare_audit.kafka.event;

import lombok.Builder;

@Builder
public record DepartmentUpdatedEvent(

        Long userId,
        String username,
        String action,
        String entityName,
        String status,
        String details
) {
}
