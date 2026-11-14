package com.healthcare.healthcare_api.shared.feignClient;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalAuthFeignConfig {

    @Bean
    public RequestInterceptor internalServiceRequestInterceptor(
            @Value("${auth-service.internal-key}") String internalServiceKey) {

        return requestTemplate -> {
            if (requestTemplate.path().startsWith("/api/v1/auth/internal/")) {
                requestTemplate.header("X-Internal-Service-Key", internalServiceKey);
            }
        };
    }
}
