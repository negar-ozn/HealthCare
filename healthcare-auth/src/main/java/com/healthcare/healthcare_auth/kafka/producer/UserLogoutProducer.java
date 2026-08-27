package com.healthcare.healthcare_auth.kafka.producer;

import com.healthcare.healthcare_auth.kafka.event.UserLogoutEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLogoutProducer {

    public static final String TOPIC = "user-logout";
    private final KafkaTemplate<String, UserLogoutEvent> kafkaTemplate;

    public UserLogoutProducer(KafkaTemplate<String, UserLogoutEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserLogoutEvent(UserLogoutEvent event) {

        kafkaTemplate.send(TOPIC, event);
        log.info("");
    }
}
