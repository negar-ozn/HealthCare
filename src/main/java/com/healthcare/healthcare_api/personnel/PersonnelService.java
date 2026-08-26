package com.healthcare.healthcare_api.personnel;

import com.healthcare.healthcare_api.shared.enums.Gender;
import com.healthcare.healthcare_api.shared.enums.PersonnelRoleName;
import com.healthcare.healthcare_api.shared.exception.NoSuchPersonnelFoundException;
import com.healthcare.healthcare_api.shared.exception.PersonnelAlreadyExistException;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final IdentifierGeneratorService identifierGeneratorService;

    public PersonnelService(PersonnelRepository personnelRepository, IdentifierGeneratorService identifierGeneratorService) {
        this.personnelRepository = personnelRepository;
        this.identifierGeneratorService = identifierGeneratorService;
    }

    // By Admin
    public Personnel addPersonnel(PersonnelRequest request) {

        personnelRepository.findById(request.id())
                .orElseThrow(() -> new PersonnelAlreadyExistException("Personnel already existed"));
        Personnel personnel = Personnel.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(personnelRoleIdentifier(request.role()))
                .personnelCode(identifierGeneratorService.generatePersonnelIdentifier())
                .department(request.department())
                .birthdate(request.birthdate())
                .gender(genderIdentifier(request.gender()))
                .nationalId(request.nationalId())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
//                .version()
                .build();
        return personnelRepository.save(personnel);
    }

    // By Admin
    public Personnel editPersonnelByAdmin(Long id, PersonnelRequest request) {

        Personnel personnelOp = personnelRepository.findById(id)
                .orElseThrow(() -> new NoSuchPersonnelFoundException("No such personnel found"));
        Personnel personnel = Personnel.builder()
                .id(personnelOp.getId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(personnelOp.getRole())
                .personnelCode(personnelOp.getPersonnelCode())
                .department(request.department())
//                .version()
                .birthdate(request.birthdate())
                .gender(Gender.valueOf(request.gender()))
                .nationalId(request.nationalId())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .build();
        return personnelRepository.save(personnel);
    }

    // By Personnel
    public Personnel editPersonnel(Long id, PersonnelRequest request) {

        Personnel personnelOp = personnelRepository.findById(id)
                .orElseThrow(() -> new NoSuchPersonnelFoundException("No such personnel found"));
        Personnel personnel = Personnel.builder()
                .id(personnelOp.getId())
                .firstName(request.firstName())
                .lastName(request.lastName())
//                .version()
                .birthdate(request.birthdate())
                .gender(Gender.valueOf(request.gender()))
                .nationalId(request.nationalId())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .build();
        return personnelRepository.save(personnel);
    }

    // By Personnel, Admin
    public PersonnelResponse getPersonnelByAdmin(Long id) {

        Personnel personnelOp = personnelRepository.findById(id)
                .orElseThrow(() -> new NoSuchPersonnelFoundException("No such personnel found"));
        return PersonnelResponse.builder()
                .firstName(personnelOp.getFirstName())
                .lastName(personnelOp.getLastName())
                .birthdate(personnelOp.getBirthdate())
                .gender(String.valueOf(personnelOp.getGender()))
                .nationalId(personnelOp.getNationalId())
                .phone(personnelOp.getPhone())
                .email(personnelOp.getEmail())
                .address(personnelOp.getAddress())
                .role(String.valueOf(personnelOp.getRole()))
                .personnelCode(personnelOp.getPersonnelCode())
                .department(personnelOp.getDepartment())
                .build();
    }

    // By other personnel
    public PersonnelResponse getPersonnel(Long id) {

        Personnel personnelOp = personnelRepository.findById(id)
                .orElseThrow(() -> new NoSuchPersonnelFoundException("No such personnel found"));
        return PersonnelResponse.builder()
                .firstName(personnelOp.getFirstName())
                .lastName(personnelOp.getLastName())
                .phone(personnelOp.getPhone())
                .email(personnelOp.getEmail())
                .department(personnelOp.getDepartment())
                .build();
    }


    // By Staff, Doctor, Admin
    public List<PersonnelResponse> findAllStaffs(String firstName, String lastName) {

        List<Personnel> personnelList = personnelRepository.findByFirstNameAndLastName(firstName, lastName);
        if (personnelList.isEmpty())
            throw new NoSuchPersonnelFoundException("No such personnel found");

        return personnelList.stream().map(personnel -> PersonnelResponse.builder()
                .firstName(personnel.getFirstName())
                .lastName(personnel.getLastName())
                .role(String.valueOf(personnel.getRole()))
                .department(personnel.getDepartment())
                .build()).toList();
    }

    // By Admin
    public void deleteStaff(Long id) {

        Personnel personnelOp = personnelRepository.findById(id)
                .orElseThrow(() -> new NoSuchPersonnelFoundException("No such personnel found."));
        personnelRepository.deleteById(personnelOp.getId());
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
}
