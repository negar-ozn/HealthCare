package com.healthcare.healthcare_auth.config;

import com.healthcare.healthcare_auth.kafka.event.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, NotificationEvent> notificationProducerFactory(KafkaProperties properties) {

        Map<String, Object> config = new HashMap<>(properties.buildProducerProperties());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate(ProducerFactory<String,
            NotificationEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, UserRegisteredEvent> userRegisteredEventProducerFactory(KafkaProperties properties) {

        Map<String, Object> config = new HashMap<>(properties.buildProducerProperties());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, UserRegisteredEvent> userRegisteredEventKafkaTemplate(ProducerFactory<String,
            UserRegisteredEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, UserLogoutEvent> userLogoutEventProducerFactory(KafkaProperties properties) {

        Map<String, Object> config = new HashMap<>(properties.buildProducerProperties());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, UserLogoutEvent> userLogoutEventKafkaTemplate(ProducerFactory<String,
            UserLogoutEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, UserLoginEvent> userLoginEventProducerFactory(KafkaProperties properties) {

        Map<String, Object> config = new HashMap<>(properties.buildProducerProperties());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, UserLoginEvent> userLoginEventKafkaTemplate(ProducerFactory<String,
            UserLoginEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, RefreshTokenEvent> refreshTokenEventProducerFactory(KafkaProperties properties) {

        Map<String, Object> config  = new HashMap<>(properties.buildProducerProperties());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, RefreshTokenEvent> refreshTokenEventKafkaTemplate(ProducerFactory<String,
            RefreshTokenEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, PasswordResetEvent> passwordResetEventProducerFactory(KafkaProperties properties) {

        Map<String, Object> config  = new HashMap<>(properties.buildProducerProperties());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, PasswordResetEvent> passwordResetEventKafkaTemplate(ProducerFactory<String,
            PasswordResetEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }
}
