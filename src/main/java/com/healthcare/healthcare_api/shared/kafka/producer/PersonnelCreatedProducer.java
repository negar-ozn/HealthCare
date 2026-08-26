package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PersonnelCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelCreatedProducer {

    public static final String TOPIC = "personnel-created";
    private final KafkaTemplate<String, PersonnelCreatedEvent> kafkaTemplate;

    public PersonnelCreatedProducer(KafkaTemplate<String, PersonnelCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPersonnelCreatedEvent(PersonnelCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
