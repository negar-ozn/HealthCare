package com.healthcare.healthcare_api.doctor;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_api.department.Department;
import com.healthcare.healthcare_api.department.DepartmentRepository;
import com.healthcare.healthcare_api.personnel.Personnel;
import com.healthcare.healthcare_api.personnel.PersonnelRepository;
import com.healthcare.healthcare_api.security.SecurityUtils;
import com.healthcare.healthcare_api.shared.enums.ActionType;
import com.healthcare.healthcare_api.shared.enums.PersonnelRoleName;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PersonnelRepository personnelRepository;
    private final DepartmentRepository departmentRepository;
    private final CommonKafkaProducer commonKafkaProducer;

    public DoctorService(DoctorRepository doctorRepository, PersonnelRepository personnelRepository,
                         DepartmentRepository departmentRepository, CommonKafkaProducer commonKafkaProducer) {
        this.doctorRepository = doctorRepository;
        this.personnelRepository = personnelRepository;
        this.departmentRepository = departmentRepository;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    @Transactional
    public DoctorResponse addDoctor(DoctorRequest doctorRequest) {

        personnelRepository.findById(doctorRequest.personnelId())
                .orElseThrow(() -> new PersonnelAlreadyExistException("The personnel already exist."));

        Doctor savedDoctor = doctorRepository.save(Doctor.builder()
                .personnel(personnelInfo(doctorRequest.personnelId()))
                .specialization(doctorRequest.specialization())
                .licenceNumber(doctorRequest.licenceNumber())
                .status(doctorRequest.status())
                .department(departmentInfo(doctorRequest.departmentId()))
                .createdAt(Instant.now())
                .build());

        commonKafkaProducer.publishDoctorCreatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DOCTOR_CREATE.name())
                .entityName(EntityName.DOCTOR)
                .status(Status.SUCCESSFUL.name())
                .details("Doctor " + savedDoctor.getPersonnel().getFirstName() +
                        " " + savedDoctor.getPersonnel().getLastName() + " created successfully")
                .eventType(EventType.DOCTOR_CREATED)
                .build());

        return doctorResponse(savedDoctor);
    }

    @Transactional
    public DoctorResponse editDoctor(Long id, DoctorRequest doctorRequest) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        doctor.setSpecialization(doctorRequest.specialization());
        doctor.setLicenceNumber(doctorRequest.licenceNumber());
        doctor.setStatus(doctorRequest.status());
        doctor.setDepartment(departmentInfo(doctorRequest.departmentId()));
        doctor.setPersonnel(personnelInfo(doctorRequest.personnelId()));

        commonKafkaProducer.publishDoctorUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DOCTOR_UPDATE.name())
                .entityName(EntityName.DOCTOR)
                .status(Status.SUCCESSFUL.name())
                .details("Doctor " + doctor.getPersonnel().getFirstName() + " " + doctor.getPersonnel().getLastName() +
                        " created successfully")
                .eventType(EventType.DOCTOR_UPDATED)
                .build());

        return doctorResponse(doctor);
    }

    @Transactional
    public DoctorResponse getDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        commonKafkaProducer.publishDoctorReadProducer(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DOCTOR_READ.name())
                .entityName(EntityName.DOCTOR)
                .status(Status.SUCCESSFUL.name())
                .details("Doctor info retrieved successfully")
                .eventType(EventType.DOCTOR_READ)
                .build());

        return doctorResponse(doctor);
    }

    @Transactional
    public List<DoctorResponse> allDoctors(String firstname, String lastname, String specialization, Long licenceNumber) {

        List<Doctor> doctorList = doctorRepository.findByFirstNameOrLastNameOrSpecializationOrLicenceNumber(
                firstname, lastname, specialization, licenceNumber);

        if (doctorList.isEmpty())
            throw new RecordNotFoundException("no such record found");

        commonKafkaProducer.publishDoctorReadProducer(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DOCTOR_READ.name())
                .entityName(EntityName.DOCTOR)
                .status(Status.SUCCESSFUL.name())
                .details("Doctors info retrieved successfully")
                .eventType(EventType.DOCTOR_READ)
                .build());

        return doctorList.stream().map(this::doctorResponse).toList();
    }

    @Transactional
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        doctorRepository.deleteById(doctor.getId());

        commonKafkaProducer.publishDoctorDeletedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DOCTOR_DELETE.name())
                .entityName(EntityName.DOCTOR)
                .status(Status.SUCCESSFUL.name())
                .details("Doctor deleted successfully")
                .eventType(EventType.DOCTOR_DELETED)
                .build());
    }

    private DoctorResponse doctorResponse(Doctor doctor) {

        return new DoctorResponse(
                doctor.getSpecialization(),
                doctor.getDepartment(),
                doctor.getLicenceNumber(),
                doctor.getStatus(),
                doctor.getPersonnel()
        );
    }

    private Personnel personnelInfo(Long personnelId) {

        return personnelRepository.findByIdAndRole(personnelId, PersonnelRoleName.ROLE_DOCTOR)
                .orElseThrow(() -> new PersonnelNotFoundException("Personnel not found."));
    }

    private Department departmentInfo(Long departmentId) {

        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found."));
    }
}
