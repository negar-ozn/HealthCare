package com.healthcare.healthcare_audit.kafka.consumer;


import com.healthcare.healthcare_audit.kafka.event.EmailSentEvent;
import com.healthcare.healthcare_audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailSentConsumer {

    private final AuditService auditService;

    public EmailSentConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "email-sent")
    public void consume(EmailSentEvent event) {

        auditService.createEmailSentEvent(event);
    }
}
