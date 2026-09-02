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
            "PATIENT_CREATED", "PATIENT_UPDATED", "PATIENT_DELETED", "PATIENT_READ",
            "DOCTOR_CREATED", "DOCTOR_UPDATED", "DOCTOR_DELETED", "DOCTOR_READ",
            "APPOINTMENT_BOOKED", "APPOINTMENT_UPDATED", "APPOINTMENT_CANCELED", "APPOINTMENT_COMPLETED",
            "APPOINTMENT_READ",
            "DEPARTMENT_CREATED", "DEPARTMENT_UPDATED", "DEPARTMENT_READ",
            "MEDICAL_RECORD_CREATED", "MEDICAL_RECORD_UPDATED", "MEDICAL_RECORD_COMPLETED",
            "MEDICAL_RECORD_READ",
            "PAYMENT_CREATED", "PAYMENT_READ",
            "PERSONNEL_CREATED", "PERSONNEL_UPDATED", "PERSONNEL_DELETED", "PERSONNEL_READ",
            "SCHEDULE_CREATED", "SCHEDULE_UPDATED", "SCHEDULE_DELETED", "SCHEDULE_READ",
            "USER_LOGIN", "USER_LOGOUT", "REFRESH_TOKEN", "PASSWORD_RESET",
    })
    public void consume(CoreEvent event) {
        auditService.createEvent(event);
    }
}
