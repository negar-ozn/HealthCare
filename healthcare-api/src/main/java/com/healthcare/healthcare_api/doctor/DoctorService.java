package com.healthcare.healthcare_api.doctor;

import com.healthcare.healthcare_api.shared.exception.DoctorAlreadyExistsException;
import com.healthcare.healthcare_api.shared.exception.NoSuchRecordFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    //By Admin
    public Doctor addDoctor(DoctorRequest doctorRequest) {

        doctorRepository.findById(doctorRequest.id())
                .orElseThrow(() -> new DoctorAlreadyExistsException("The Doctor already exists."));
        Doctor doctor = Doctor.builder()
                .personnel(doctorRequest.personnel())
                .specialization(doctorRequest.specialization())
                .licenceNumber(doctorRequest.licenceNumber())
                .availabilitySchedule(doctorRequest.availabilitySchedule())
                .status(doctorRequest.status())
                .department(doctorRequest.department())
                .createdAt(Instant.now())
//                .version()
                .build();
        return doctorRepository.save(doctor);
    }

    //By Admin
    public Doctor editDoctor(Long id, DoctorRequest doctorRequest) {

        Doctor doctorOp = doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        Doctor doctor = Doctor.builder()
                .id(doctorOp.getId())
                .specialization(doctorRequest.specialization())
                .licenceNumber(doctorRequest.licenceNumber())
                .availabilitySchedule(doctorRequest.availabilitySchedule())
                .status(doctorRequest.status())
                .department(doctorRequest.department())
                .personnel(doctorRequest.personnel())
                .updatedAt(Instant.now())
//                .version()
                .build();
        return doctorRepository.save(doctor);
    }

    //BY Admin
    public DoctorResponse getDoctor(Long id) {

        Doctor doctorOp = doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        return DoctorResponse.builder()
                .specialization(doctorOp.getSpecialization())
                .department(doctorOp.getDepartment())
                .licenceNumber(doctorOp.getLicenceNumber())
                .availabilitySchedule(doctorOp.getAvailabilitySchedule())
                .status(doctorOp.getStatus())
                .personnel(doctorOp.getPersonnel())
                .createdAt(doctorOp.getCreatedAt())
                .updatedAt(doctorOp.getUpdatedAt())
                .build();
    }

    // By doctor, staff, patient, and admin
    public List<DoctorResponse> allDoctors(String firstname, String lastname, String specialization, Long licenceNumber) {

        List<Doctor> doctorList =
                doctorRepository.findByFirstNameOrLastNameOrSpecializationOrLicenceNumber(firstname, lastname, specialization, licenceNumber);
        if (doctorList.isEmpty()) {
            throw new NoSuchRecordFoundException("no such record found");
        }
        return doctorList.stream().map(doctor -> DoctorResponse.builder()
                .specialization(doctor.getSpecialization())
                .department(doctor.getDepartment())
                .licenceNumber(doctor.getLicenceNumber())
                .availabilitySchedule(doctor.getAvailabilitySchedule())
                .status(doctor.getStatus())
                .personnel(doctor.getPersonnel())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build()).toList();
    }

    //By Admin
    public void deleteDoctor(Long id) {

        Doctor doctorOp = doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        doctorRepository.deleteById(doctorOp.getId());
    }
}
