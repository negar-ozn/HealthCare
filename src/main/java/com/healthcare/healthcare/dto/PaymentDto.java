package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Invoice;
import com.healthcare.healthcare.enums.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Long id;
    private Invoice invoice;
    private Double amount;
    private Method method;
    private LocalDate paymentDate;
}
