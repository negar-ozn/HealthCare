package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.UserLogoutEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLogoutConsumer {

    private final AuditService auditService;

    public UserLogoutConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "user-logout")
    public void consume(UserLogoutEvent event) {

        auditService.createUserLogoutLog(event);
    }
}
