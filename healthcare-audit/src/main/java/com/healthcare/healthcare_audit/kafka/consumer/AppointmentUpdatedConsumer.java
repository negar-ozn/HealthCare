package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.AppointmentUpdatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentUpdatedConsumer {

    private final AuditService auditService;

    public AppointmentUpdatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "appointment-updated")
    public void consume(AppointmentUpdatedEvent event) {

        auditService.createAppointmentUpdatedLog(event);
    }
}
