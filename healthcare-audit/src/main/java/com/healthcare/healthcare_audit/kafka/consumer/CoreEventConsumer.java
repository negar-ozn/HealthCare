package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CoreEventConsumer {

    private final AuditService auditService;

    public CoreEventConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = {
            "patient-created", "patient-updated", "patient-deleted", "patient-read",
            "doctor-created", "doctor-updated", "doctor-deleted", "doctor-read",
            "appointment-booked", "appointment-updated", "appointment-canceled", "appointment-completed",
            "appointment-read",
            "department-created", "department-updated", "department-read",
            "medical-record-created", "medical-record-updated", "medical-record-completed",
            "medical-record-read",
            "payment-created", "payment-read",
            "personnel-created", "personnel-updated", "personnel-deleted", "personnel-read",
            "schedule-created", "schedule-updated", "schedule-deleted", "schedule-read",
            "user-login", "user-logout", "refresh-token", "introspect", "email-verified", "email-sent",
            "new-password", "forget-password-email"
    },
            containerFactory = "coreKafkaListenerContainerFactory")
    public void consume(CoreEvent event) {
        auditService.createEvent(event);
    }
}
