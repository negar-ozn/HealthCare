package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.RefreshTokenEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RefreshTokenConsumer {

    private final AuditService auditService;

    public RefreshTokenConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "refresh-token")
    public void consume(RefreshTokenEvent event) {

        auditService.createRefreshTokenLog(event);
    }
}
