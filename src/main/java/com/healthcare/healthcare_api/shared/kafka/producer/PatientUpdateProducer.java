package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PatientUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientUpdateProducer {

    public static final String TOPIC = "patient-update";
    private final KafkaTemplate<String, PatientUpdatedEvent> kafkaTemplate;

    public PatientUpdateProducer(KafkaTemplate<String, PatientUpdatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPatientUpdatedEvent(PatientUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
