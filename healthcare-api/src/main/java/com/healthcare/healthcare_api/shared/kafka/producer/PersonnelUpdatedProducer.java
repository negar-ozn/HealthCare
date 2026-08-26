package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PersonnelUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelUpdatedProducer {

    public static final String TOPIC = "personnel-updated";
    private final KafkaTemplate<String, PersonnelUpdatedEvent> kafkaTemplate;

    public PersonnelUpdatedProducer(KafkaTemplate<String, PersonnelUpdatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPersonnelUpdatedEvent(PersonnelUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
