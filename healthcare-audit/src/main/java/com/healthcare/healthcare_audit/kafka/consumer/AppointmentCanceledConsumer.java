package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.AppointmentCanceledEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentCanceledConsumer {

    private final AuditService auditService;

    public AppointmentCanceledConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "appointment-canceled")
    public void consume(AppointmentCanceledEvent event) {

        auditService.createAppointmentCanceledLog(event);
    }
}
