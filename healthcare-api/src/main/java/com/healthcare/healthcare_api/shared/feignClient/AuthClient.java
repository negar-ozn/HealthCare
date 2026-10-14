package com.healthcare.healthcare_api.shared.feignClient;


import com.healthcare.healthcare_api.shared.internalUserCreationDto.InternalUserCreateRequest;
import com.healthcare.healthcare_api.shared.internalUserCreationDto.InternalUserCreateResponse;
import com.healthcare.healthcare_api.shared.tokenIntrospectDto.TokenIntrospectionRequest;
import com.healthcare.healthcare_api.shared.tokenIntrospectDto.TokenIntrospectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "healthcare-auth", url = "${auth-service.url}",
        configuration = InternalAuthFeignConfig.class)
public interface AuthClient {

    @PostMapping("/api/v1/auth/introspect")
    TokenIntrospectionResponse introspect(TokenIntrospectionRequest request);

    @PostMapping("/api/v1/auth/internal/users")
    InternalUserCreateResponse createInternalUser(InternalUserCreateRequest request);
}
