package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PersonnelUpdatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelUpdatedConsumer {

    private final AuditService auditService;

    public PersonnelUpdatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "personnel-updated")
    public void consume(PersonnelUpdatedEvent event) {

        auditService.createPersonnelUpdatedLog(event);
    }
}
