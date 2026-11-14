package com.healthcare.healthcare_api.department.cache;

import com.healthcare.healthcare_api.department.Department;
import com.healthcare.healthcare_api.department.DepartmentRepository;
import com.healthcare.healthcare_api.department.DepartmentResponse;
import com.healthcare.healthcare_api.shared.exception.RecordNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentCacheService {

    private final DepartmentRepository departmentRepository;

    public DepartmentCacheService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "departmentById", key = "#id", sync = true)
    public DepartmentResponse getDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The department is not available"));

        return DepartmentResponse.builder()
                .name(department.getName())
                .message(department.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "departments", sync = true)
    public List<DepartmentResponse> allDepartments() {

        List<Department> departmentList = departmentRepository.findAll();

        if (departmentList.isEmpty()) {
            throw new RecordNotFoundException("The department is not available");
        }

        return departmentList.stream().map(department -> DepartmentResponse.builder()
                .name(department.getName())
                .message(department.getDescription())
                .build()).toList();
    }
}
