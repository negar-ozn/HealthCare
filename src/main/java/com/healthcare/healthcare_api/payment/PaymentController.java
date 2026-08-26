package com.healthcare.healthcare_api.payment;

import com.healthcare.healthcare_api.shared.enums.Method;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiDescription;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiResponseCode;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = ApiDescription.PAYMENT, description = ApiDescription.PAYMENT_API)
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = ApiSummary.NEW_PAYMENT, description = ApiDescription.NEW_PAYMENT)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PAYMENT_CREATION_SUCCESS))
    @PreAuthorize("hasAuthority('PATIENT_CREATE')")
    @PostMapping("/newPayment")
    public ResponseEntity<PaymentRequest> addPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentRequest payment = paymentService.addPayment(paymentRequest);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = ApiSummary.FIND_PAYMENT, description = ApiDescription.FIND_PAYMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PAYMENT_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PAYMENT_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('PATIENT_READ', 'RECEPTIONIST_READ', 'ADMIN_READ')")
    @GetMapping("/getPayment/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        PaymentResponse payment = paymentService.getPayment(id);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = ApiSummary.ALL_PAYMENTS, description = ApiDescription.ALL_PAYMENTS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PAYMENTS_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PAYMENTS_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('PATIENT_READ', 'RECEPTIONIST_READ', 'ADMIN_READ')")
    @GetMapping("/allPayments")
    public ResponseEntity<PaymentResponse> allPayments(@RequestParam(required = false) Long id,
                                                       @RequestParam(required = false) Method method,
                                                       @RequestParam(required = false) LocalDate paymentDate) {
        PaymentResponse payment = paymentService.allPayment(id, method, paymentDate);
        return ResponseEntity.ok(payment);
    }
}
