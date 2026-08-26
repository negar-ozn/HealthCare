package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.AppointmentBookedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentBookedConsumer {

    private final AuditService auditService;

    public AppointmentBookedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }


    @KafkaListener(topics = "appointment-booked")
    public void consume(AppointmentBookedEvent event) {

        auditService.createAppointmentBookedLog(event);
    }
}
