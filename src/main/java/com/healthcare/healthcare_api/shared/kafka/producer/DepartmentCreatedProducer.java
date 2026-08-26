package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.DepartmentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentCreatedProducer {

    public static final String TOPIC = "department-created";
    private final KafkaTemplate<String, DepartmentCreatedEvent> kafkaTemplate;

    public DepartmentCreatedProducer(KafkaTemplate<String, DepartmentCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDepartmentCreatedEvent(DepartmentCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
