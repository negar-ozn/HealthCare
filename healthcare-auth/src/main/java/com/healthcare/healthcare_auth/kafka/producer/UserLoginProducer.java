package com.healthcare.healthcare_auth.kafka.producer;

import com.healthcare.healthcare_auth.kafka.event.UserLoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLoginProducer {

    public static final String TOPIC = "user-login";
    private final KafkaTemplate<String, UserLoginEvent> kafkaTemplate;

    public UserLoginProducer(KafkaTemplate<String, UserLoginEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserLoginEvent(UserLoginEvent event) {

        kafkaTemplate.send(TOPIC, event);
        log.info("");
    }
}
