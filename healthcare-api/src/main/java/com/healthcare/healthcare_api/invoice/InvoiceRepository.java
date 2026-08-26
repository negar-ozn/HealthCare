package com.healthcare.healthcare_api.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT I FROM Invoice I WHERE I.id=:id OR I.patient.patientNumber=:patientNumber " +
            "OR I.appointment.appointmentNumber=:appointmentNumber OR I.createdAt=:issueDate")
    List<Invoice> findAllByIdOrPatient_PatientNumberOrAppointment_AppointmentNumberOrCreatedAt(Long id, Long patientNumber,
                                                                                               Long appointmentNumber,
                                                                                               Instant issueDate);
}
