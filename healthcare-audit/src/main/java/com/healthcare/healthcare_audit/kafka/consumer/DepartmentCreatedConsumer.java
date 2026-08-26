package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.DepartmentCreatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentCreatedConsumer {

    private final AuditService auditService;

    public DepartmentCreatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "department-created")
    public void consume(DepartmentCreatedEvent event) {

        auditService.createDepartmentCreatedLog(event);
    }
}
