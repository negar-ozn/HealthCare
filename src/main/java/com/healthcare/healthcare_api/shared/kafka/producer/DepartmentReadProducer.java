package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.DepartmentReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentReadProducer {

    public static final String TOPIC = "department-read";
    private final KafkaTemplate<String, DepartmentReadEvent> kafkaTemplate;

    public DepartmentReadProducer(KafkaTemplate<String, DepartmentReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDepartmentReadEvent(DepartmentReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
