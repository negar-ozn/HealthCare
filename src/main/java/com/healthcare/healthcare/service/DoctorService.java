package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.DoctorDto;
import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.exception.DoctorAlreadyExistsException;
import com.healthcare.healthcare.exception.NoSuchRecordFoundException;
import com.healthcare.healthcare.repository.DoctorRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    //By Admin
    public Doctor addDoctor(DoctorDto doctorDto) {
        Optional<Doctor> optional = doctorRepository.findById(doctorDto.getId());
        if (optional.isPresent()) {
            try {
                throw new DoctorAlreadyExistsException("The Doctor already exists.");
            } catch (DoctorAlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        }
        Doctor doctor = new Doctor();
        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setLicenceNumber(doctorDto.getLicenceNumber());
        doctor.setAvailabilitySchedule(doctorDto.getAvailabilitySchedule());
        doctor.setStatus(doctorDto.getStatus());
        doctor.setDepartment(doctorDto.getDepartment());
        doctor.setUserInfo(doctorDto.getUserInfo());
        return doctorRepository.save(doctor);
    }

    //By Admin
    public Doctor editDoctor(Long id, DoctorDto doctorDto) {
        Optional<Doctor> optional = doctorRepository.findById(doctorDto.getId());
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Doctor doctor = optional.get();
        doctor.setId(id);
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setLicenceNumber(doctorDto.getLicenceNumber());
        doctor.setAvailabilitySchedule(doctorDto.getAvailabilitySchedule());
        doctor.setStatus(doctorDto.getStatus());
        doctor.setDepartment(doctorDto.getDepartment());
        doctor.setUserInfo(doctorDto.getUserInfo());
        return doctorRepository.save(doctor);
    }

    //BY Admin
    public DoctorDto getDoctor(Long id) {
        Optional<Doctor> optional = doctorRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Doctor doctor = optional.get();
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(doctor.getId());
        doctorDto.setLastName(doctor.getLastName());
        doctorDto.setFirstName(doctor.getFirstName());
        doctorDto.setSpecialization(doctor.getSpecialization());
        doctorDto.setLicenceNumber(doctor.getLicenceNumber());
        doctorDto.setAvailabilitySchedule(doctor.getAvailabilitySchedule());
        doctorDto.setStatus(doctor.getStatus());
        doctorDto.setDepartment(doctor.getDepartment());
        doctorDto.setUserInfo(doctor.getUserInfo());
        return doctorDto;
    }

    // By doctor, staff, patient, and admin
    public List<DoctorDto> allDoctors(String firstname, String lastname, String specialization, Long licenceNumber) {
        List<Doctor> doctorList =
                doctorRepository.findByFirstNameOrLastNameOrSpecializationOrLicenceNumber(firstname, lastname, specialization, licenceNumber);
        if (doctorList.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        List<DoctorDto> doctorDtoList = new ArrayList<>();
        DoctorDto doctorDto = new DoctorDto();
        for (Doctor doctor : doctorList) {
            doctorDto.setLicenceNumber(doctor.getLicenceNumber());
            doctorDto.setFirstName(doctor.getFirstName());
            doctorDto.setLastName(doctor.getLastName());
            doctorDto.setSpecialization(doctor.getSpecialization());
            doctorDto.setAvailabilitySchedule(doctor.getAvailabilitySchedule());
            doctorDto.setStatus(doctor.getStatus());
            doctorDto.setDepartment(doctor.getDepartment());
            doctorDto.setUserInfo(doctor.getUserInfo());
            doctorDto.setId(doctor.getId());
            doctorDtoList.add(doctorDto);
        }
        return doctorDtoList;
    }

    //By Admin
    public void deleteDoctor(Long id) {
        Optional<Doctor> optional = doctorRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Doctor doctor = optional.get();
        doctorRepository.deleteById(doctor.getId());
    }
}
