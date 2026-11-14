package com.healthcare.healthcare_api.payment;

import com.healthcare.healthcare_api.shared.enums.Method;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentService {
    public PaymentRequest addPayment(PaymentRequest paymentRequest) {
        return null;
    }

    public PaymentResponse getPayment(Long id) {
        return null;
    }

    public PaymentResponse allPayment(Long id, Method method, LocalDate paymentDate) {
        return null;
    }
}
