package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.ScheduleDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleDeletedProducer {

    public static final String TOPIC = "schedule-deleted";
    private final KafkaTemplate<String, ScheduleDeletedEvent> kafkaTemplate;

    public ScheduleDeletedProducer(KafkaTemplate<String, ScheduleDeletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishScheduleDeletedEvent(ScheduleDeletedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
