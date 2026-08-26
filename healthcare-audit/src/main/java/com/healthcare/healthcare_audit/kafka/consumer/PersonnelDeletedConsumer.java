package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PersonnelDeletedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelDeletedConsumer {

    private final AuditService auditService;

    public PersonnelDeletedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "personnel-deleted")
    public void consume(PersonnelDeletedEvent event) {

        auditService.createPersonnelDeletedLog(event);
    }
}
