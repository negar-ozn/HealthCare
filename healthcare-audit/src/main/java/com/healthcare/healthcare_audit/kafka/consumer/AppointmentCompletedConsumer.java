package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.AppointmentCompletedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentCompletedConsumer {

    private final AuditService auditService;

    public AppointmentCompletedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "appointment-completed")
    public void consume(AppointmentCompletedEvent event) {

        auditService.createAppointmentCompletedLog(event);
    }
}
