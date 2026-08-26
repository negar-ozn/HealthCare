package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.MedicalRecordReadEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordReadConsumer {

    private final AuditService auditService;

    public MedicalRecordReadConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "medical-record-read")
    public void consume(MedicalRecordReadEvent event) {

        auditService.createMedicalRecordReadLog(event);
    }
}
