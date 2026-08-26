package com.healthcare.healthcare_api.shared.kafka.producer;

import com.healthcare.healthcare_api.shared.kafka.event.PaymentReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentReadProducer {

    public static final String TOPIC = "payment-read";
    private final KafkaTemplate<String, PaymentReadEvent> kafkaTemplate;

    public PaymentReadProducer(KafkaTemplate<String, PaymentReadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentReadEvent(PaymentReadEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}
