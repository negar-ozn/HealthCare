package com.healthcare.healthcare_api.shared.config;

import com.healthcare.healthcare_api.department.DepartmentResponse;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.TypeFactory;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisSerializer<?> departmentSerializer = new JacksonJsonRedisSerializer<>(DepartmentResponse.class);

        JavaType departmentListType = TypeFactory.createDefaultInstance()
                .constructCollectionType(List.class, DepartmentResponse.class);

        RedisSerializer<Object> departmentsSerializer = new JacksonJsonRedisSerializer<>(departmentListType);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));

        RedisCacheConfiguration departmentByIdConfig =
                defaultConfig.serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(departmentSerializer));

        RedisCacheConfiguration departmentsConfig =
                defaultConfig.serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(departmentsSerializer));

        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);

        return RedisCacheManager.builder(cacheWriter)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("departmentById", departmentByIdConfig)
                .withCacheConfiguration("departments", departmentsConfig)
                .build();
    }

}
