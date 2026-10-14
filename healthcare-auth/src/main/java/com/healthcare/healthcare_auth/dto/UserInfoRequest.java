package com.healthcare.healthcare_auth.dto;

import lombok.Builder;

@Builder
public record UserInfoRequest(

        Long userId
) {
}
