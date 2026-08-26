package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.DepartmentUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentUpdatedProducer {

    public static final String TOPIC = "department-updated";
    private final KafkaTemplate<String, DepartmentUpdatedEvent> kafkaTemplate;

    public DepartmentUpdatedProducer(KafkaTemplate<String, DepartmentUpdatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDepartmentUpdatedEvent(DepartmentUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
