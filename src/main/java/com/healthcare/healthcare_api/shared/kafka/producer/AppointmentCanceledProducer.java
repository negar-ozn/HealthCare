package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.AppointmentCanceledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentCanceledProducer {

    public static final String TOPIC = "appointment-canceled";
    private final KafkaTemplate<String, AppointmentCanceledEvent> kafkaTemplate;

    public AppointmentCanceledProducer(KafkaTemplate<String, AppointmentCanceledEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAppointmentCanceledEvent(AppointmentCanceledEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
