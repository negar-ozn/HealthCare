package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.PaymentDto;
import com.healthcare.healthcare.enums.Method;
import com.healthcare.healthcare.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController("/v1/api/Payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/newPayment")
    public ResponseEntity<PaymentDto> addPayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto payment = paymentService.addPayment(paymentDto);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("/getPayment/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long id) {
        PaymentDto payment = paymentService.getPayment(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("/allPayments")
    public ResponseEntity<PaymentDto> allPayments(@RequestParam(required = false) Long id,
                                                  @RequestParam(required = false) Method method,
                                                  @RequestParam(required = false) LocalDate paymentDate) {
        PaymentDto payment = paymentService.allPayment(id, method, paymentDate);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
