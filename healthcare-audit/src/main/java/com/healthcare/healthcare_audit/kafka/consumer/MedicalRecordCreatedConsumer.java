package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.MedicalRecordCreatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordCreatedConsumer {

    private final AuditService auditService;

    public MedicalRecordCreatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "medical-record-created")
    public void consume(MedicalRecordCreatedEvent event) {

        auditService.createMedicalRecordCreatedLog(event);
    }
}
