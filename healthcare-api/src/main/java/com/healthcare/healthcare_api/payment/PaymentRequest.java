package com.healthcare.healthcare_api.payment;

import com.healthcare.healthcare_api.invoice.Invoice;
import com.healthcare.healthcare_api.shared.enums.Method;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record PaymentRequest (

    Long id,
    Invoice invoice,
    @NotNull BigDecimal amount,
    @NotNull Method method,
    @NotNull LocalDate paymentDate
){
}
