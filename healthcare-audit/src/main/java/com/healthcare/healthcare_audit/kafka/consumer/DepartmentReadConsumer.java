package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.DepartmentReadEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentReadConsumer {

    private final AuditService auditService;

    public DepartmentReadConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "department-read")
    public void consume(DepartmentReadEvent event) {

        auditService.createDepartmentReadLog(event);
    }
}
