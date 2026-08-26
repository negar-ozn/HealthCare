package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.ScheduleReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleReadProducer {

    public static final String TOPIC = "schedule-read";
    private final KafkaTemplate<String, ScheduleReadEvent> kafkaTemplate;

    public ScheduleReadProducer(KafkaTemplate<String, ScheduleReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishScheduleReadEvent(ScheduleReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
