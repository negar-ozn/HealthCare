package com.healthcare.healthcare_api.payment;

import com.healthcare.healthcare_api.invoice.Invoice;
import com.healthcare.healthcare_api.shared.enums.Method;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record PaymentResponse(

        Invoice invoice,
        BigDecimal amount,
        Method method,
        LocalDate paymentDate
) {
}
