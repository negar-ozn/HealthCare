package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.UserRegisteredEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRegisteredConsumer {

    private final AuditService auditService;

    public UserRegisteredConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "user-registered")
    public void consume(UserRegisteredEvent event) {

        auditService.createUserRegisteredLog(event);
    }
}
