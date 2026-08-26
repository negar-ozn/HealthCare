package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.DepartmentUpdatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentUpdatedConsumer {

    private final AuditService auditService;

    public DepartmentUpdatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "department-updated")
    public void consume(DepartmentUpdatedEvent event) {

        auditService.createDepartmentUpdatedLog(event);
    }
}
