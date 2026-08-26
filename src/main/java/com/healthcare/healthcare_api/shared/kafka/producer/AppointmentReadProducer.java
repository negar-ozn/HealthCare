package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.AppointmentReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentReadProducer {

    public static final String TOPIC = "appointment-read";
    private final KafkaTemplate<String, AppointmentReadEvent> kafkaTemplate;

    public AppointmentReadProducer(KafkaTemplate<String, AppointmentReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAppointmentReadEvent(AppointmentReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
