package com.healthcare.healthcare_api.shared.kafka.event;

import lombok.Builder;

@Builder
public record MedicalRecordUpdatedEvent(

        Long userId,
        String username,
        String action,
        String entityName,
        String status,
        String details
) {
}
