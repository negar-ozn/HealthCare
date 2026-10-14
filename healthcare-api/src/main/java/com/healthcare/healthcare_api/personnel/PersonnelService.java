package com.healthcare.healthcare_api.personnel;

import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.healthcare_api.department.Department;
import com.healthcare.healthcare_api.department.DepartmentRepository;
import com.healthcare.healthcare_api.security.SecurityUtils;
import com.healthcare.healthcare_api.shared.internalUserCreationDto.InternalUserCreateRequest;
import com.healthcare.healthcare_api.shared.internalUserCreationDto.InternalUserCreateResponse;
import com.healthcare.healthcare_api.shared.enums.ActionType;
import com.healthcare.healthcare_api.shared.enums.Gender;
import com.healthcare.healthcare_api.shared.enums.PersonnelRoleName;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.DepartmentNotFoundException;
import com.healthcare.healthcare_api.shared.exception.PersonnelNotFoundException;
import com.healthcare.healthcare_api.shared.feignClient.AuthClient;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final IdentifierGeneratorService identifierGeneratorService;
    private final DepartmentRepository departmentRepository;
    private final AuthClient authClient;
    private final CommonKafkaProducer commonKafkaProducer;

    public PersonnelService(PersonnelRepository personnelRepository, IdentifierGeneratorService identifierGeneratorService,
                            DepartmentRepository departmentRepository, AuthClient authClient, CommonKafkaProducer commonKafkaProducer) {
        this.personnelRepository = personnelRepository;
        this.identifierGeneratorService = identifierGeneratorService;
        this.departmentRepository = departmentRepository;
        this.authClient = authClient;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    @Transactional
    public PersonnelResponse addPersonnel(PersonnelRequest request) {

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found: " + request.departmentId()));

        InternalUserCreateResponse userResponse = authClient.createInternalUser(InternalUserCreateRequest.builder()
                .username(request.username())
                .email(request.email())
                .role(request.role())
                .build());

        Personnel savedPersonnel = personnelRepository.save(Personnel.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .birthdate(request.birthdate())
                .gender(genderIdentifier(request.gender()))
                .nationalId(Long.valueOf(request.nationalId()))
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .role(personnelRoleIdentifier(request.role()))
                .personnelCode(identifierGeneratorService.generatePersonnelIdentifier())
                .department(department)
                .userId(userResponse.userId())
                .build());

        commonKafkaProducer.publishPersonnelCreatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PERSONNEL_CREATE.name())
                .entityName(EntityName.PERSONNEL)
                .status(Status.SUCCESSFUL.name())
                .details("Personnel with '" + savedPersonnel.getRole().name() + "' role created successfully")
                .eventType(EventType.PERSONNEL_CREATED)
                .build());

        return personnelResponse(savedPersonnel);
    }

    @Transactional
    public PersonnelResponse editPersonnel(Long id, PersonnelRequest request) {

        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new PersonnelNotFoundException("No such personnel found"));

        personnel.setFirstName(request.firstName());
        personnel.setLastName(request.lastName());
        personnel.setRole(personnel.getRole());
        personnel.setPersonnelCode(personnel.getPersonnelCode());
        personnel.setBirthdate(request.birthdate());
        personnel.setGender(Gender.valueOf(request.gender()));
        personnel.setNationalId(Long.valueOf(request.nationalId()));
        personnel.setPhone(request.phone());
        personnel.setEmail(request.email());
        personnel.setAddress(request.address());

        commonKafkaProducer.publishPersonnelUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PERSONNEL_UPDATE.name())
                .entityName(EntityName.PERSONNEL)
                .status(Status.SUCCESSFUL.name())
                .details("Personnel with '" + personnel.getRole().name() + "' role updated successfully")
                .eventType(EventType.PERSONNEL_UPDATED)
                .build());

        return personnelResponse(personnel);
    }

    @Transactional
    public PersonnelResponse findPersonnel(Long id) {

        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new PersonnelNotFoundException("No such personnel found"));

        commonKafkaProducer.publishPersonnelReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PERSONNEL_READ.name())
                .entityName(EntityName.PERSONNEL)
                .status(Status.SUCCESSFUL.name())
                .details("Personnel with '" + personnel.getRole().name() + "' role found successfully")
                .eventType(EventType.PERSONNEL_READ)
                .build());

        return personnelResponse(personnel);
    }

    @Transactional
    public List<PersonnelResponse> getAllPersonnelByRole(String personnelRole) {

        List<Personnel> personnelList = personnelRepository.findByRole(PersonnelRoleName.valueOf(personnelRole));

        if (personnelList.isEmpty())
            throw new PersonnelNotFoundException("No such personnel found");

        commonKafkaProducer.publishPersonnelReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PERSONNEL_READ.name())
                .entityName(EntityName.PERSONNEL)
                .status(Status.SUCCESSFUL.name())
                .details("All personnel with " + personnelRole + " found successfully")
                .eventType(EventType.PERSONNEL_READ)
                .build());

        return personnelList.stream().map(this::personnelResponse).toList();
    }

    @Transactional
    public List<PersonnelResponse> getAllPersonnel() {

        List<Personnel> personnelList = personnelRepository.findAll();

        if (personnelList.isEmpty())
            throw new PersonnelNotFoundException("No such personnel found");

        commonKafkaProducer.publishPersonnelReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PERSONNEL_READ.name())
                .entityName(EntityName.PERSONNEL)
                .status(Status.SUCCESSFUL.name())
                .details("All personnel found successfully")
                .eventType(EventType.PERSONNEL_READ)
                .build());

        return personnelList.stream().map(this::personnelResponse).toList();
    }

    @Transactional
    public void deletePersonnel(Long id) {

        Personnel personnelOp = personnelRepository.findById(id)
                .orElseThrow(() -> new PersonnelNotFoundException("No such personnel found."));

        personnelRepository.deleteById(personnelOp.getId());

        commonKafkaProducer.publishPersonnelDeletedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PERSONNEL_DELETE.name())
                .entityName(EntityName.PERSONNEL)
                .status(Status.SUCCESSFUL.name())
                .details("Personnel with '" + personnelOp.getRole().name() + "' deleted successfully")
                .eventType(EventType.PERSONNEL_DELETED)
                .build());
    }

    @Transactional
    public PersonnelResponse getMyProfile(Long userId) {

        Personnel personnelInfo = personnelRepository.findByUserId(userId)
                .orElseThrow(() -> new PersonnelNotFoundException("Personnel profile not found for user: " + userId));

        commonKafkaProducer.publishProfileReadSelfEvent(CoreEvent.builder()
                .userId(personnelInfo.getUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PROFILE_READ_SELF.name())
                .entityName(EntityName.PERSONNEL)
                .status(Status.SUCCESSFUL.name())
                .details("All personnel found successfully")
                .eventType(EventType.PROFILE_READ_SELF)
                .build());

        return personnelResponse(personnelInfo);
    }

    public Gender genderIdentifier(String gender) {

        return switch (gender) {
            case "FEMALE" -> Gender.FEMALE;
            case "MALE" -> Gender.MALE;
            default -> throw new IllegalStateException("Unexpected value: " + gender);
        };
    }

    private PersonnelRoleName personnelRoleIdentifier(String role) {

        return switch (role) {
            case "ROLE_ADMIN" -> PersonnelRoleName.ROLE_ADMIN;
            case "ROLE_DOCTOR" -> PersonnelRoleName.ROLE_DOCTOR;
            case "ROLE_NURSE" -> PersonnelRoleName.ROLE_NURSE;
            case "ROLE_RECEPTIONIST" -> PersonnelRoleName.ROLE_RECEPTIONIST;
            case "ROLE_LAB_TECHNICIAN" -> PersonnelRoleName.ROLE_LAB_TECHNICIAN;
            default -> throw new IllegalStateException("Unexpected value: " + role);
        };
    }

    private PersonnelResponse personnelResponse(Personnel personnel) {

        return new PersonnelResponse(
                personnel.getFirstName(),
                personnel.getLastName(),
                personnel.getBirthdate(),
                personnel.getGender().name(),
                personnel.getNationalId(),
                personnel.getPhone(),
                personnel.getEmail(),
                personnel.getAddress(),
                personnel.getRole().name(),
                personnel.getPersonnelCode(),
                personnel.getDepartment(),
                personnel.getCreatedAt(),
                personnel.getUpdatedAt());
    }
}
