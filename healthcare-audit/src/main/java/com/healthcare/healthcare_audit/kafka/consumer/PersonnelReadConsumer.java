package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PersonnelReadEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelReadConsumer {

    private final AuditService auditService;

    public PersonnelReadConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "personnel-read")
    public void consume(PersonnelReadEvent event) {

        auditService.createPersonnelReadLog(event);
    }
}
