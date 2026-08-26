package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.AppointmentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentCompletedProducer {

    public static final String TOPIC = "appointment-completed";
    private final KafkaTemplate<String, AppointmentCompletedEvent> kafkaTemplate;

    public AppointmentCompletedProducer(KafkaTemplate<String, AppointmentCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAppointmentCompletedEvent(AppointmentCompletedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
