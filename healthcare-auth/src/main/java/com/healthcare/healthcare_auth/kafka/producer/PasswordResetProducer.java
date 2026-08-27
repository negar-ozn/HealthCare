package com.healthcare.healthcare_auth.kafka.producer;

import com.healthcare.healthcare_auth.kafka.event.PasswordResetEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordResetProducer {

    public static final String TOPIC = "password-reset";
    private final KafkaTemplate<String, PasswordResetEvent> kafkaTemplate;

    public PasswordResetProducer(KafkaTemplate<String, PasswordResetEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPasswordResetEvent(PasswordResetEvent event) {

        kafkaTemplate.send(TOPIC, event);
        log.info("");
    }
}
