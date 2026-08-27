package com.healthcare.healthcare_auth.kafka.producer;

import com.healthcare.healthcare_auth.kafka.event.RefreshTokenEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RefreshTokenProducer {

    public static final String TOPIC = "refresh-token";
    private final KafkaTemplate<String, RefreshTokenEvent> kafkaTemplate;

    public RefreshTokenProducer(KafkaTemplate<String, RefreshTokenEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRefreshTokenEvent(RefreshTokenEvent event) {

        kafkaTemplate.send(TOPIC, event);
        log.info("");
    }
}
