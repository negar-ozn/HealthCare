package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PersonnelCreatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelCreatedConsumer {

    private final AuditService auditService;

    public PersonnelCreatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "personnel-created")
    public void consume(PersonnelCreatedEvent event) {

        auditService.createPersonnelCreatedLog(event);
    }
}
