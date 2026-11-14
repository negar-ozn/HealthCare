package com.healthcare.common_kafka.event;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import lombok.Builder;

@Builder
public record CoreEvent (

        Long userId,
        String email,
        String username,
        String action,
        EntityName entityName,
        String status,
        String details,
        EventType eventType
) {
}
