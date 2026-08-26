package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PatientCreatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientCreatedConsumer {

    private final AuditService auditService;

    public PatientCreatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "patient-created")
    public void consume(PatientCreatedEvent event) {

        auditService.createPatientCreatedLog(event);
    }
}
