package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PaymentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentCreatedProducer {

    public static final String TOPIC = "payment-created";
    private final KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate;

    public PaymentCreatedProducer(KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentCreatedEvent(PaymentCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
