package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PaymentCreatedEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentCreatedConsumer {

    private final AuditService auditService;

    public PaymentCreatedConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "payment-created")
    public void consume(PaymentCreatedEvent event) {

        auditService.createPaymentCreatedLog(event);
    }
}
