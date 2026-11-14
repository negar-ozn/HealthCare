package com.healthcare.healthcare_audit.kafka.config;

import com.healthcare.common_kafka.event.UserRegisteredEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Configuration
public class UserRegisteredConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public UserRegisteredConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, UserRegisteredEvent> userRegisteredConsumerFactory() {

        JacksonJsonDeserializer<UserRegisteredEvent> deserializer = new JacksonJsonDeserializer<>(UserRegisteredEvent.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> userRegisteredKafkaListenerContainerFactory(
            ConsumerFactory<String, UserRegisteredEvent> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
