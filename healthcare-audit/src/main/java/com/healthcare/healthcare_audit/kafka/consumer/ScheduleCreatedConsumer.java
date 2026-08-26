package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.ScheduleCreatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleCreatedConsumer {

    private final AuditService auditService;

    public ScheduleCreatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "schedule-created")
    public void consume(ScheduleCreatedEvent event) {

        auditService.createScheduleCreatedLog(event);
    }
}
