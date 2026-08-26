package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.MedicalRecordCompletedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordCompletedConsumer {

    private final AuditService auditService;

    public MedicalRecordCompletedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "medical-record-completed")
    public void consume(MedicalRecordCompletedEvent event) {

        auditService.createMedicalRecordCompletedLog(event);
    }
}
