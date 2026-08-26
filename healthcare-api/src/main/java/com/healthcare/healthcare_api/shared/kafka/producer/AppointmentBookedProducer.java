package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.AppointmentBookedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentBookedProducer {

    public static final String TOPIC = "appointment-booked";
    private final KafkaTemplate<String, AppointmentBookedEvent> kafkaTemplate;

    public AppointmentBookedProducer(KafkaTemplate<String, AppointmentBookedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAppointmentBookedEvent(AppointmentBookedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
