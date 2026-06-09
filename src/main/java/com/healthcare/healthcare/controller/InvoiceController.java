package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.InvoiceDto;
import com.healthcare.healthcare.entitiy.Appointment;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/v1/api/Invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/newInvoice")
    public ResponseEntity<InvoiceDto> addInvoice(@RequestBody InvoiceDto invoiceDto) {
        InvoiceDto invoice = invoiceService.addInvoice(invoiceDto);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PutMapping("/editInvoice/{id}")
    public ResponseEntity<InvoiceDto> editInvoice(@PathVariable Long id) {
        InvoiceDto invoice = invoiceService.editInvoice(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("/getInvoice/{id}")
    public ResponseEntity<InvoiceDto> getInvoice(@PathVariable Long id) {
        InvoiceDto invoice = invoiceService.getInvoice(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("/allInvoices")
    public ResponseEntity<InvoiceDto> allInvoice(@RequestParam(required = false) Long id,
                                                 @RequestParam(required = false) Patient patient,
                                                 @RequestParam(required = false)Appointment appointment) {
        InvoiceDto invoice = invoiceService.allInvoices(id, patient, appointment);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @DeleteMapping("/deleteInvoice/{id}")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }
}
