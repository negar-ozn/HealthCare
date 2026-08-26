package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.AppointmentReadEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentReadConsumer {

    private final AuditService auditService;

    public AppointmentReadConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "appointment-read")
    public void consume(AppointmentReadEvent event) {

        auditService.createAppointmentReadLog(event);
    }
}
