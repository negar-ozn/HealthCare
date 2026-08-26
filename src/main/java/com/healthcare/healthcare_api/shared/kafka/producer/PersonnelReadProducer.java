package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PersonnelReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelReadProducer {

    public static final String TOPIC = "personnel-read";
    private final KafkaTemplate<String, PersonnelReadEvent> kafkaTemplate;

    public PersonnelReadProducer(KafkaTemplate<String, PersonnelReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPersonnelReadEvent(PersonnelReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
