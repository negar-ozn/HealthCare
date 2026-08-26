package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.DoctorUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorUpdatedProducer {

    public static final String TOPIC = "doctor-updated";
    private final KafkaTemplate<String, DoctorUpdatedEvent> kafkaTemplate;

    public DoctorUpdatedProducer(KafkaTemplate<String, DoctorUpdatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDoctorUpdatedEvent(DoctorUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
