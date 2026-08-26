package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.ScheduleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleCreatedProducer {

    public static final String TOPIC = "schedule-created";
    private final KafkaTemplate<String, ScheduleCreatedEvent> kafkaTemplate;

    public ScheduleCreatedProducer(KafkaTemplate<String, ScheduleCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishScheduleCreatedEvent(ScheduleCreatedEvent event){

        kafkaTemplate.send(TOPIC, event);
    }
}
