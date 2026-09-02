package com.healthcare.healthcare_audit.kafka.config;

import com.healthcare.common_kafka.event.EmailSentEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Configuration
public class EmailSentConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public EmailSentConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, EmailSentEvent> emailSentConsumerFactory() {

        JacksonJsonDeserializer<EmailSentEvent> deserializer = new JacksonJsonDeserializer<>(EmailSentEvent.class);
        deserializer.addTrustedPackages("com.healthcare.common_kafka.event");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailSentEvent> emailSentKafkaListenerContainerFactory(
            ConsumerFactory<String, EmailSentEvent> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailSentEvent>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
