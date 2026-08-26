package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.ScheduleDeletedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleDeletedConsumer {

    private final AuditService auditService;

    public ScheduleDeletedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "schedule-deleted")
    public void consume(ScheduleDeletedEvent event) {

        auditService.createScheduleDeletedLog(event);
    }
}
