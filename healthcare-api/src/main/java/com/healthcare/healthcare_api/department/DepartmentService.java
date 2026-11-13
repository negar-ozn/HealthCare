package com.healthcare.healthcare_api.department;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_api.department.cache.DepartmentCacheInvalidationEvent;
import com.healthcare.healthcare_api.department.cache.DepartmentCacheService;
import com.healthcare.healthcare_api.security.SecurityUtils;
import com.healthcare.healthcare_api.shared.enums.ActionType;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.DepartmentAlreadyExistsException;
import com.healthcare.healthcare_api.shared.exception.RecordNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CommonKafkaProducer commonKafkaProducer;
    private final DepartmentCacheService departmentCacheService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public DepartmentService(DepartmentRepository departmentRepository, CommonKafkaProducer commonKafkaProducer,
                             DepartmentCacheService departmentCacheService, ApplicationEventPublisher applicationEventPublisher) {

        this.departmentRepository = departmentRepository;
        this.commonKafkaProducer = commonKafkaProducer;
        this.departmentCacheService = departmentCacheService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public DepartmentResponse addDepartment(CreateDepartmentRequest createDepartmentRequest) {

        Optional<Department> departmentOp = departmentRepository.findByName(createDepartmentRequest.name());
        if (departmentOp.isPresent())
            throw new DepartmentAlreadyExistsException("The department is already exists.");

        Department department = departmentRepository.save(Department.builder()
                .name(createDepartmentRequest.name())
                .description(createDepartmentRequest.description())
                .createdAt(Instant.now())
                .build());

        commonKafkaProducer.publishDepartmentCreatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DEPARTMENT_CREATE.name())
                .entityName(EntityName.DEPARTMENT)
                .status(Status.SUCCESSFUL.name())
                .details(department.getName() + " department created successfully.")
                .eventType(EventType.DEPARTMENT_CREATED)
                .build());

        applicationEventPublisher.publishEvent(new DepartmentCacheInvalidationEvent(department.getId()));

        return DepartmentResponse.builder()
                .name(department.getName())
                .message("The " + department.getName() + " department created successfully.")
                .build();
    }

    @Transactional
    public DepartmentResponse editDepartment(Long id, UpdateDepartmentRequest updateDepartmentRequest) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The department is not available"));

        department.setName(updateDepartmentRequest.name());
        department.setDescription(updateDepartmentRequest.description());

        commonKafkaProducer.publishDepartmentUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DEPARTMENT_UPDATE.name())
                .entityName(EntityName.DEPARTMENT)
                .status(Status.SUCCESSFUL.name())
                .details(updateDepartmentRequest.name() + " department updated successfully.")
                .eventType(EventType.DEPARTMENT_UPDATED)
                .build());

        applicationEventPublisher.publishEvent(new DepartmentCacheInvalidationEvent(id));

        return DepartmentResponse.builder()
                .name(updateDepartmentRequest.name())
                .message(updateDepartmentRequest.name() + " department updated successfully.")
                .build();
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(Long id) {

        DepartmentResponse departmentResponse = departmentCacheService.getDepartment(id);

        commonKafkaProducer.publishDepartmentReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DEPARTMENT_READ.name())
                .entityName(EntityName.DEPARTMENT)
                .status(Status.SUCCESSFUL.name())
                .details(departmentResponse.name() + " department retrieved successfully.")
                .eventType(EventType.DEPARTMENT_READ)
                .build());

        return departmentResponse;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> allDepartments() {

        List<DepartmentResponse> departments = departmentCacheService.allDepartments();

        commonKafkaProducer.publishDepartmentReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DEPARTMENT_READ.name())
                .entityName(EntityName.DEPARTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("All departments retrieved successfully.")
                .eventType(EventType.DEPARTMENT_READ)
                .build());

        return departments;
    }

    @Transactional
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The department is not available"));

        departmentRepository.deleteById(department.getId());

        commonKafkaProducer.publishDepartmentDeletedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.DEPARTMENT_DELETE.name())
                .entityName(EntityName.DEPARTMENT)
                .status(Status.SUCCESSFUL.name())
                .details(department.getName() + " department deleted successfully.")
                .eventType(EventType.DEPARTMENT_DELETED)
                .build());

        applicationEventPublisher.publishEvent(new DepartmentCacheInvalidationEvent(id));

    }
}
