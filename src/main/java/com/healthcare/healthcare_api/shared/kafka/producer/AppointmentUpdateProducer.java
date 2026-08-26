package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.AppointmentUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentUpdateProducer {

    public static final String TOPIC = "appointment-updated";
    private final KafkaTemplate<String, AppointmentUpdatedEvent> kafkaTemplate;

    public AppointmentUpdateProducer(KafkaTemplate<String, AppointmentUpdatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAppointmentUpdatedEvent(AppointmentUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
