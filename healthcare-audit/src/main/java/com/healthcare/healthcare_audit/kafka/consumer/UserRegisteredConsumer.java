package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.common_kafka.event.UserRegisteredEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredConsumer {

    private final AuditService auditService;

    public UserRegisteredConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "user-registered",
            groupId = "healthcare-audit",
            containerFactory = "userRegisteredKafkaListenerContainerFactory")
    public void consume(UserRegisteredEvent event) {

        auditService.createUserRegisteredLog(event);
    }
}
