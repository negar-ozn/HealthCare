package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.MedicalRecordCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordCompletedProducer {

    public static final String TOPIC = "medical-record-completed";
    private final KafkaTemplate<String, MedicalRecordCompletedEvent> kafkaTemplate;

    public MedicalRecordCompletedProducer(KafkaTemplate<String, MedicalRecordCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMedicalRecordCompletedEvent(MedicalRecordCompletedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
