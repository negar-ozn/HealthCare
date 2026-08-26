package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.ScheduleReadEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleReadConsumer {

    private final AuditService auditService;

    public ScheduleReadConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "schedule-read")
    public void consume(ScheduleReadEvent event) {

        auditService.createScheduleReadLog(event);
    }
}
