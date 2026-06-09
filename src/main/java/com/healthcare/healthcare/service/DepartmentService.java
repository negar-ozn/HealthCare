package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.DepartmentDto;
import com.healthcare.healthcare.entitiy.Department;
import com.healthcare.healthcare.exception.DepartmentAlreadyExistsException;
import com.healthcare.healthcare.exception.NoSuchRecordFoundException;
import com.healthcare.healthcare.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    //All services run by Admin!!

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department addDepartment(DepartmentDto departmentDto) {
        Optional<Department> optional = departmentRepository.findById(departmentDto.getId());
        if (optional.isPresent()) {
            try {
                throw new DepartmentAlreadyExistsException("The department is already exists.");
            } catch (DepartmentAlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        }
        Department department = new Department();
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());
        return departmentRepository.save(department);
    }

    public Department editDepartment(Long id, DepartmentDto departmentDto) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("The department is not available");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Department department = optional.get();
        department.setId(departmentDto.getId());
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());
        return departmentRepository.save(department);
    }

    public DepartmentDto getDepartment(Long id) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("The department is not available");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Department department = optional.get();
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName(department.getName());
        departmentDto.setDescription(department.getDescription());
        return departmentDto;
    }

    public List<DepartmentDto> allDepartments(String name) {
        List<Department> departmentList = departmentRepository.findAllByName(name);
        if (departmentList.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("The department is not available");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        for (Department department : departmentList){
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setName(department.getName());
            departmentDto.setDescription(departmentDto.getDescription());
            departmentDtoList.add(departmentDto);
        }
        return departmentDtoList;
    }

    public void deleteDepartment(Long id) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("The department is not available");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Department department = optional.get();
        departmentRepository.deleteById(department.getId());
    }
}
