package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.MedicalRecordReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MedicalRecordReadProducer {

    public static final String TOPIC = "medical-record-read";
    private final KafkaTemplate<String, MedicalRecordReadEvent> kafkaTemplate;

    public MedicalRecordReadProducer(KafkaTemplate<String, MedicalRecordReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMedicalRecordReadEvent(MedicalRecordReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
