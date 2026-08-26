package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PersonnelDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelDeletedProducer {

    public static final String TOPIC = "personnel-deleted";
    private final KafkaTemplate<String, PersonnelDeletedEvent> kafkaTemplate;

    public PersonnelDeletedProducer(KafkaTemplate<String, PersonnelDeletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPersonnelDeletedEvent(PersonnelDeletedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
