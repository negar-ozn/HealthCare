package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.InvoiceDto;
import com.healthcare.healthcare.entitiy.Appointment;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceDto addInvoice(InvoiceDto invoiceDto) {
        return null;
    }

    public InvoiceDto editInvoice(Long id) {
        return null;
    }

    public InvoiceDto getInvoice(Long id) {
        return null;
    }

    public InvoiceDto allInvoices(Long id, Patient patient, Appointment appointment) {
        return null;
    }

    public void deleteInvoice(Long id) {

    }
}
