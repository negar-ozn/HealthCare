package com.healthcare.healthcare_api.shared.feignClient;


import com.healthcare.healthcare_api.shared.tokenIntrospectDto.TokenIntrospectionRequest;
import com.healthcare.healthcare_api.shared.tokenIntrospectDto.TokenIntrospectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "healthcare-auth", url = "${auth-service.url}")
public interface AuthClient {

    @PostMapping("/api/auth/introspect")
    TokenIntrospectionResponse introspect(TokenIntrospectionRequest request);
}
