package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.MedicalRecordUpdatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordUpdatedConsumer {

    private final AuditService auditService;

    public MedicalRecordUpdatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "medical-record-updated")
    public void consume(MedicalRecordUpdatedEvent event) {

        auditService.createMedicalRecordUpdatedLog(event);
    }
}
