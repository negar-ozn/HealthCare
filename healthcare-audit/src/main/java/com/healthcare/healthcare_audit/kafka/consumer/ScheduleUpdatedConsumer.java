package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.ScheduleUpdatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleUpdatedConsumer {

    private final AuditService auditService;

    public ScheduleUpdatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "schedule-update")
    public void consume(ScheduleUpdatedEvent event) {

        auditService.createScheduleUpdatedLog(event);
    }
}
