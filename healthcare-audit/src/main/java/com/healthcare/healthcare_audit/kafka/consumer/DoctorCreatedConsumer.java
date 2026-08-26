package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.DoctorCreatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorCreatedConsumer {

    private final AuditService auditService;

    public DoctorCreatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "doctor-created")
    public void consume(DoctorCreatedEvent event) {

        auditService.createDoctorCreatedLog(event);
    }
}
