package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.MedicalRecordCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordCreatedProducer {

    public static final String TOPIC = "medical-record-created";
    private final KafkaTemplate<String, MedicalRecordCreatedEvent> kafkaTemplate;

    public MedicalRecordCreatedProducer(KafkaTemplate<String, MedicalRecordCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMedicalRecordCreatedEvent(MedicalRecordCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
