package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PatientCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientCreatedProducer {

    public static final String TOPIC = "patient-created";
    private final KafkaTemplate<String, PatientCreatedEvent> kafkaTemplate;

    public PatientCreatedProducer(KafkaTemplate<String, PatientCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPatientCreatedEvent(PatientCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
