package com.healthcare.common_kafka.config;

import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.common_kafka.event.UserRegisteredEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
public class CommonKafkaAutoConfiguration {

    private Map<String, Object> producerProperties(KafkaProperties kafkaProperties) {

        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return properties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "coreEventKafkaTemplate")
    public KafkaTemplate<String, CoreEvent> coreEventKafkaTemplate(KafkaProperties kafkaProperties) {

        ProducerFactory<String, CoreEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProperties(kafkaProperties));
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnMissingBean(name = "notificationEventKafkaTemplate")
    public KafkaTemplate<String, NotificationEvent> notificationEventKafkaTemplate(KafkaProperties kafkaProperties) {

        ProducerFactory<String, NotificationEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProperties(kafkaProperties));
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnMissingBean(name = "userRegisteredEventKafkaTemplate")
    public KafkaTemplate<String, UserRegisteredEvent> userRegisteredEventKafkaTemplate(KafkaProperties kafkaProperties) {

        ProducerFactory<String, UserRegisteredEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProperties(kafkaProperties));
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonKafkaProducer commonKafkaProducer(
            KafkaTemplate<String, CoreEvent> coreEventKafkaTemplate,
            KafkaTemplate<String, NotificationEvent> notificationEventKafkaTemplate,
            KafkaTemplate<String, UserRegisteredEvent> userRegisteredEventKafkaTemplate) {

        return new CommonKafkaProducer(coreEventKafkaTemplate, notificationEventKafkaTemplate, userRegisteredEventKafkaTemplate
        );
    }
}
