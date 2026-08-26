package com.healthcare.healthcare_audit.kafka.consumer;

import com.healthcare.healthcare_audit.kafka.event.PaymentReadEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentReadConsumer {

    private final AuditService auditService;

    public PaymentReadConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "payment-read")
    public void consume(PaymentReadEvent event) {

        auditService.createPaymentReadLog(event);
    }
}
