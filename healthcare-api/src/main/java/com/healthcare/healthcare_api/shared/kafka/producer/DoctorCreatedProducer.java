package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.DoctorCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorCreatedProducer {

    public static final String TOPIC = "doctor-created";
    private final KafkaTemplate<String, DoctorCreatedEvent> kafkaTemplate;

    public DoctorCreatedProducer(KafkaTemplate<String, DoctorCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDoctorCreatedEvent(DoctorCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
