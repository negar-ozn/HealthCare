package com.healthcare.healthcare_audit.kafka.config;

import com.healthcare.common_kafka.event.CoreEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Configuration
public class CoreConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public CoreConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, CoreEvent> coreConsumerFactory() {

        JacksonJsonDeserializer<CoreEvent> deserializer = new JacksonJsonDeserializer<>(CoreEvent.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CoreEvent> coreKafkaListenerContainerFactory(
            ConsumerFactory<String, CoreEvent> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, CoreEvent>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
