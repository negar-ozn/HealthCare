package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PatientUpdatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientUpdatedConsumer {

    private final AuditService auditService;

    public PatientUpdatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "patient-update")
    public void consume(PatientUpdatedEvent event) {

        auditService.createPatientUpdatedLog(event);
    }
}
