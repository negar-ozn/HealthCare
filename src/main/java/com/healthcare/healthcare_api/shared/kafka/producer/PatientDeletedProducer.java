package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PatientDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientDeletedProducer {

    public static final String TOPIC = "patient-deleted";
    private final KafkaTemplate<String, PatientDeletedEvent> kafkaTemplate;

    public PatientDeletedProducer(KafkaTemplate<String, PatientDeletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPatientDeletedEvent(PatientDeletedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
