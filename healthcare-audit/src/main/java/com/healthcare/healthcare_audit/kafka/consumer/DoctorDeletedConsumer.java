package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.DoctorDeletedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorDeletedConsumer {

    private final AuditService auditService;

    public DoctorDeletedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "doctor-deleted")
    public void consume(DoctorDeletedEvent event) {

        auditService.createdDoctorDeletedLog(event);
    }
}
