package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.StaffDto;
import com.healthcare.healthcare.entitiy.Staff;
import com.healthcare.healthcare.exception.NoSuchStaffFoundException;
import com.healthcare.healthcare.exception.StaffAlreadyExistException;
import com.healthcare.healthcare.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    // By Staff
    public Staff addStaff(StaffDto staffDto) {
        Optional<Staff> optionalStaff = staffRepository.findById(staffDto.getId());
        if (optionalStaff.isPresent())
            try {
                throw new StaffAlreadyExistException("Staff already existed");
            } catch (StaffAlreadyExistException e) {
                throw new RuntimeException(e);
            }
        Staff staff = new Staff();
        staff.setFirstName(staffDto.getFirstName());
        staff.setLastName(staffDto.getLastName());
        staff.setDepartment(staffDto.getDepartment());
        staff.setRole(staffDto.getRole());
        staffRepository.save(staff);
        return staff;
    }

    // By Staff
    public Staff editStaff(Long id, StaffDto staffDto) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isEmpty())
            try {
                throw new NoSuchStaffFoundException("No such staff found");
            } catch (NoSuchStaffFoundException e) {
                throw new RuntimeException(e);
            }
        Staff staff = optionalStaff.get();
        staff.setId(staffDto.getId());
        staff.setFirstName(staffDto.getFirstName());
        staff.setLastName(staffDto.getLastName());
        staffRepository.save(staff);
        return staff;
    }

    // By Staff, Doctor, Admin
    public StaffDto getStaff(Long id) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isEmpty())
            try {
                throw new NoSuchStaffFoundException("No such staff found");
            } catch (NoSuchStaffFoundException e) {
                throw new RuntimeException(e);
            }
        Staff staff = optionalStaff.get();
        StaffDto staffDto = new StaffDto();
        staffDto.setFirstName(staff.getFirstName());
        staffDto.setLastName(staff.getLastName());
        staffDto.setRole(staff.getRole());
        staffDto.setDepartment(staff.getDepartment());
        return staffDto;
    }

    // By Staff, Doctor, Admin
    public StaffDto findAllStaffs(String firstName, String lastName) {
        Optional<Staff> optionalStaff = staffRepository.findByFirstNameAndLastName(firstName, lastName);
        if (optionalStaff.isEmpty())
            try {
                throw new NoSuchStaffFoundException("No such staff found");
            } catch (NoSuchStaffFoundException e) {
                throw new RuntimeException(e);
            }
        Staff staff = optionalStaff.get();
        StaffDto staffDto = new StaffDto();
        staffDto.setFirstName(staff.getFirstName());
        staffDto.setLastName(staff.getLastName());
        staffDto.setDepartment(staff.getDepartment());
        staffDto.setRole(staff.getRole());
        return staffDto;
    }

    // By Admin
    public void deleteStaff(Long id) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isPresent())
            staffRepository.deleteById(id);
    }
}
