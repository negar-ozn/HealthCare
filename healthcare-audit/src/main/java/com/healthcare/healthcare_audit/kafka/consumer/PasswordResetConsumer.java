package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PasswordResetEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordResetConsumer {

    private final AuditService auditService;

    public PasswordResetConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "password-reset")
    public void consume(PasswordResetEvent event) {

        auditService.createPasswordResetLog(event);
    }
}
