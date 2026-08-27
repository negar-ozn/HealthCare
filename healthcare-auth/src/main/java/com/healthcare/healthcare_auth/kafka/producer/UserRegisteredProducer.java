package com.healthcare.healthcare_auth.kafka.producer;

import com.healthcare.healthcare_auth.kafka.event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRegisteredProducer {

    public static final String TOPIC = "user-registered";
    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public UserRegisteredProducer(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {

        kafkaTemplate.send(TOPIC, event.email(), event);
    }
}
