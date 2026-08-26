package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.DoctorDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorDeletedProducer {

    public static final String TOPIC = "doctor-deleted";
    private final KafkaTemplate<String, DoctorDeletedEvent> kafkaTemplate;

    public DoctorDeletedProducer(KafkaTemplate<String, DoctorDeletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDoctorDeletedEvent(DoctorDeletedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
