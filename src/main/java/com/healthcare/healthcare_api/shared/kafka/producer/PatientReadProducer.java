package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PatientReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientReadProducer {

    public static final String TOPIC = "patient-read";
    private final KafkaTemplate<String, PatientReadEvent> kafkaTemplate;

    public PatientReadProducer(KafkaTemplate<String, PatientReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPatientReadEvent(PatientReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
