package com.healthcare.healthcare_audit.kafka.consumer;


import com.healthcare.healthcare_audit.kafka.event.UserLoginEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLoginConsumer {

    private final AuditService auditService;

    public UserLoginConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "user-login")
    public void consume(UserLoginEvent event) {

        auditService.createUserLoginLog(event);
    }
}
