package com.healthcare.healthcare_auth.kafka.producer;

import com.healthcare.healthcare_auth.kafka.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationProducer {

    public static final String TOPIC = "notification-events";
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(NotificationEvent event) {

        kafkaTemplate.send(TOPIC, event.correlationId(), event);
        log.info("Notification event published. correlationId={}", event.correlationId());
    }
}
