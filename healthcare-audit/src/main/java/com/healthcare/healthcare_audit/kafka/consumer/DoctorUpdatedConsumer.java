package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.DoctorUpdatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorUpdatedConsumer {

    private final AuditService auditService;

    public DoctorUpdatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "doctor-updated")
    public void consume(DoctorUpdatedEvent event) {

        auditService.createDoctorUpdatedLog(event);
    }
}
