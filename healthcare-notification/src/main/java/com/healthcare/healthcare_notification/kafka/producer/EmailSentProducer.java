package com.healthcare.healthcare_notification.kafka.producer;

import com.healthcare.healthcare_notification.kafka.event.EmailSentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailSentProducer {

    public static final String TOPIC = "email-sent";
    private final KafkaTemplate<String, EmailSentEvent> kafkaTemplate;

    public EmailSentProducer(KafkaTemplate<String, EmailSentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEmailSentEvent(EmailSentEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
