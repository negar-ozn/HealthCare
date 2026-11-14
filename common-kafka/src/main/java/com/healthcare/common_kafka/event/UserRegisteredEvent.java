package com.healthcare.common_kafka.event;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import lombok.Builder;

@Builder
public record UserRegisteredEvent(

        Long userId,
        String email,
        String username,
        String verificationLink,
        String action,
        String status,
        String detail,
        EntityName entityName,
        EventType eventType
) {
}
