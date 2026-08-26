package com.healthcare.healthcare_api.invoice;

import com.healthcare.healthcare_api.appointment.Appointment;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.NoSuchInvoiceFoundException;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final IdentifierGeneratorService identifierGeneratorService;

    public InvoiceService(InvoiceRepository invoiceRepository, IdentifierGeneratorService identifierGeneratorService) {
        this.invoiceRepository = invoiceRepository;
        this.identifierGeneratorService = identifierGeneratorService;
    }

    public Invoice addInvoice(InvoiceDto invoiceDto) {

        invoiceRepository.findById(invoiceDto.getId())
                .orElseThrow(() -> new NoSuchInvoiceFoundException("No such invoice found."));
        Invoice invoice = Invoice.builder()
                .amount(invoiceDto.getAmount())
                .tax(invoiceDto.getTax())
                .insuranceCoveredAmount(invoiceDto.getInsuranceCoveredAmount())
                .patientPayableAmount(invoiceDto.getPatientPayableAmount())
                .status(Status.PAID)
                .invoiceNumber(identifierGeneratorService.generateInvoiceIdentifier())
//                .version()
                .patient(invoiceDto.getPatient())
                .appointment(invoiceDto.getAppointment())
                .createdAt(Instant.now())
                .build();
        return invoiceRepository.save(invoice);
    }

    public Invoice editInvoice(Long id, InvoiceDto invoiceDto) {

        Invoice invoiceOp = invoiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchInvoiceFoundException("No such invoice found."));

        Invoice invoice = Invoice.builder()
                .id(invoiceOp.getId())
                .amount(invoiceDto.getAmount())
                .tax(invoiceDto.getTax())
                .insuranceCoveredAmount(invoiceDto.getInsuranceCoveredAmount())
                .patientPayableAmount(invoiceDto.getPatientPayableAmount())
                .status(invoiceOp.getStatus())
                .invoiceNumber(invoiceDto.getInvoiceNumber())
//                .version()
                .patient(invoiceDto.getPatient())
                .appointment(invoiceDto.getAppointment())
                .updatedAt(Instant.now())
                .build();
        return invoiceRepository.save(invoice);
    }

    public InvoiceDto getInvoice(Long id) {

        Invoice invoiceOp = invoiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchInvoiceFoundException("No such invoice found."));
        return InvoiceDto.builder()
                .id(invoiceOp.getId())
                .patient(invoiceOp.getPatient())
                .appointment(invoiceOp.getAppointment())
                .amount(invoiceOp.getAmount())
                .tax(invoiceOp.getTax())
                .insuranceCoveredAmount(invoiceOp.getInsuranceCoveredAmount())
                .patientPayableAmount(invoiceOp.getPatientPayableAmount())
                .status(invoiceOp.getStatus())
                .invoiceNumber(invoiceOp.getInvoiceNumber())
                .createdAt(invoiceOp.getCreatedAt())
                .updatedAt(invoiceOp.getUpdatedAt())
                .build();
    }

    public List<InvoiceDto> allInvoices(Long id, Long patientNumber, Long appointmentNumber, Instant issueDate) {

        List<Invoice> invoiceList
                = invoiceRepository.findAllByIdOrPatient_PatientNumberOrAppointment_AppointmentNumberOrCreatedAt(id,
                patientNumber, appointmentNumber, issueDate);
        return invoiceList.stream().map(invoice -> InvoiceDto.builder()
                .id(invoice.getId())
                .patient(invoice.getPatient())
                .appointment(invoice.getAppointment())
                .amount(invoice.getAmount())
                .tax(invoice.getTax())
                .insuranceCoveredAmount(invoice.getInsuranceCoveredAmount())
                .patientPayableAmount(invoice.getPatientPayableAmount())
                .status(invoice.getStatus())
                .invoiceNumber(invoice.getInvoiceNumber())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build()).toList();
    }

    public void deleteInvoice(Long id) {

        Invoice invoiceOp = invoiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchInvoiceFoundException("No such invoice found."));
        invoiceRepository.deleteById(invoiceOp.getId());
    }
}
