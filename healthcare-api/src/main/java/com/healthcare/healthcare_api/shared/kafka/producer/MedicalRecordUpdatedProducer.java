package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.MedicalRecordUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordUpdatedProducer {

    public static final String TOPIC = "medical-record-updated";
    private final KafkaTemplate<String, MedicalRecordUpdatedEvent> kafkaTemplate;

    public MedicalRecordUpdatedProducer(KafkaTemplate<String, MedicalRecordUpdatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMedicalRecordUpdatedEvent(MedicalRecordUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
