package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.DoctorReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorReadProducer {

    public static final String TOPIC = "doctor-read";
    private final KafkaTemplate<String, DoctorReadEvent> kafkaTemplate;

    public DoctorReadProducer(KafkaTemplate<String, DoctorReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDoctorReadProducer(DoctorReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
