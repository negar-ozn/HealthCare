package com.healthcare.healthcare_api.invoice;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController("/api/v1/Invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/newInvoice")
    public ResponseEntity<Invoice> addInvoice(@Valid @RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = invoiceService.addInvoice(invoiceDto);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/editInvoice/{id}")
    public ResponseEntity<Invoice> editInvoice(@PathVariable Long id,
                                               @Valid @RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = invoiceService.editInvoice(id, invoiceDto);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/getInvoice/{id}")
    public ResponseEntity<InvoiceDto> getInvoice(@PathVariable Long id) {
        InvoiceDto invoice = invoiceService.getInvoice(id);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/allInvoices")
    public ResponseEntity<List<InvoiceDto>> allInvoice(@RequestParam(required = false) Long id,
                                                       @RequestParam(required = false) Long patientNumber,
                                                       @RequestParam(required = false) Long appointmentNumber,
                                                       @RequestParam(required = false) Instant issueDate) {
        List<InvoiceDto> invoice = invoiceService.allInvoices(id, patientNumber, appointmentNumber, issueDate);
        return ResponseEntity.ok(invoice);
    }

    @DeleteMapping("/deleteInvoice/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
