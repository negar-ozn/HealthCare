package com.healthcare.healthcare_api.department;

import com.healthcare.healthcare_api.shared.exception.DepartmentAlreadyExistsException;
import com.healthcare.healthcare_api.shared.exception.NoSuchRecordFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    //All services run by Admin!!

    public DepartmentService(DepartmentRepository departmentRepository) {

        this.departmentRepository = departmentRepository;
    }

    public Department addDepartment(DepartmentRequest departmentRequest) {

        departmentRepository.findById(departmentRequest.id())
                .orElseThrow(() -> new DepartmentAlreadyExistsException("The department is already exists."));
        Department department = Department.builder()
                .name(departmentRequest.name())
                .description(departmentRequest.description())
                .createdAt(Instant.now())
//                .version()
                .build();
        return departmentRepository.save(department);
    }

    public Department editDepartment(Long id, DepartmentRequest departmentRequest) {

        Department departmentOp = departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("The department is not available"));
        Department department = Department.builder()
                .id(departmentOp.getId())
                .name(departmentRequest.name())
                .description(departmentRequest.description())
                .updatedAt(Instant.now())
//                .version()
                .build();
        return departmentRepository.save(department);
    }

    public DepartmentResponse getDepartment(Long id) {

        Department departmentOp = departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("The department is not available"));
        return DepartmentResponse.builder()
                .name(departmentOp.getName())
                .description(departmentOp.getDescription())
                .createdAt(departmentOp.getCreatedAt())
                .updatedAt(departmentOp.getUpdatedAt())
                .build();
    }

    public List<DepartmentResponse> allDepartments(String name) {

        List<Department> departmentList = departmentRepository.findAllByName(name);
        if (departmentList.isEmpty()) {
            throw new NoSuchRecordFoundException("The department is not available");
        }
        return departmentList.stream().map(department -> DepartmentResponse.builder()
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build()).toList();
    }

    public void deleteDepartment(Long id) {

        Department departmentOp = departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("The department is not available"));
        departmentRepository.deleteById(departmentOp.getId());
    }
}
