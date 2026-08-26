package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.ScheduleUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleUpdatedProducer {

    public static final String TOPIC = "schedule-update";
    private final KafkaTemplate<String, ScheduleUpdatedEvent> kafkaTemplate;

    public ScheduleUpdatedProducer(KafkaTemplate<String, ScheduleUpdatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishScheduleUpdatedEvent(ScheduleUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}

