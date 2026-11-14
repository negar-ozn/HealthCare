package com.healthcare.healthcare_api.schedule;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.doctor.DoctorRepository;
import com.healthcare.healthcare_api.security.SecurityUtils;
import com.healthcare.healthcare_api.shared.enums.*;
import com.healthcare.healthcare_api.shared.exception.DoctorNotFoundException;
import com.healthcare.healthcare_api.shared.exception.RecordNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final CommonKafkaProducer commonKafkaProducer;

    public ScheduleService(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository,
                           CommonKafkaProducer commonKafkaProducer) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    @Transactional
    public List<ScheduleResponse> createSchedule(ScheduleCreateRequest request) {

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found."));

        LocalTime startTime = request.startTime();
        LocalTime endTime = request.endTime();
        int slotDuration = request.slotDurationMinutes();

        // 1. Validate time range
        if (!startTime.isBefore(endTime))
            throw new IllegalArgumentException("Start time must be before end time.");

        // 2. Validate slot duration
        if (slotDuration <= 0)
            throw new IllegalArgumentException("Slot duration must be greater than zero.");

        // 3. Make sure the range divides exactly into slots
        long totalMinutes = Duration.between(startTime, endTime).toMinutes();

        if (totalMinutes % slotDuration != 0)
            throw new IllegalArgumentException("The time range must be divisible by the slot duration.");

        // 4. Generate slots
        List<Schedule> schedules = new ArrayList<>();
        LocalTime slotStart = startTime;

        while (slotStart.isBefore(endTime)) {

            LocalTime slotEnd = slotStart.plusMinutes(slotDuration);

            Schedule schedule = Schedule.builder()
                    .doctor(doctor)
                    .weekday(Weekday.valueOf(request.date().getDayOfWeek().name()))
                    .date(request.date())
                    .startTime(slotStart)
                    .endTime(slotEnd)
                    .status(ScheduleStatus.AVAILABLE)
                    .build();

            schedules.add(schedule);
            slotStart = slotEnd;
        }

        // 5. Save all generated slots
        List<Schedule> savedSchedules = scheduleRepository.saveAllAndFlush(schedules);

        // 6. Produce event
        commonKafkaProducer.publishScheduleCreatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.SCHEDULE_CREATE.name())
                .entityName(EntityName.SCHEDULE)
                .status(Status.SUCCESSFUL.name())
                .details("Schedule slots created successfully.")
                .eventType(EventType.SCHEDULE_CREATED)
                .build());

        // 7. Convert to response
        return savedSchedules.stream()
                .map(this::scheduleResponse)
                .toList();
    }

    @Transactional
    public ScheduleResponse editSchedule(Long id, ScheduleRequest scheduleRequest) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        schedule.setWeekday(scheduleRequest.weekday());
        schedule.setDate(scheduleRequest.date());
        schedule.setStartTime(scheduleRequest.startTime());
        schedule.setEndTime(scheduleRequest.endTime());
        schedule.setStatus(scheduleRequest.status());
        schedule.setDoctor(scheduleRequest.doctor());

        commonKafkaProducer.publishScheduleUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.SCHEDULE_UPDATE.name())
                .entityName(EntityName.SCHEDULE)
                .status(Status.SUCCESSFUL.name())
                .details("Schedule slot updated successfully.")
                .eventType(EventType.SCHEDULE_UPDATED)
                .build());

        return scheduleResponse(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAvailableSchedules(Long doctorId, LocalDate date) {

        List<AppointmentStatus> activeStatusList = List.of(
                AppointmentStatus.SCHEDULED,
                AppointmentStatus.CONFIRMED,
                AppointmentStatus.CHECKED_IN,
                AppointmentStatus.IN_PROGRESS
        );

        commonKafkaProducer.publishScheduleReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.SCHEDULE_READ.name())
                .entityName(EntityName.SCHEDULE)
                .status(Status.SUCCESSFUL.name())
                .details("All available schedules retrieved successfully.")
                .eventType(EventType.SCHEDULE_READ)
                .build());

        return scheduleRepository.findAvailableSlots(doctorId, date, ScheduleStatus.AVAILABLE, activeStatusList)
                .stream().map(this::scheduleResponse).toList();
    }

    @Transactional
    public ScheduleResponse getSchedule(Long id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        commonKafkaProducer.publishScheduleReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.SCHEDULE_READ.name())
                .entityName(EntityName.SCHEDULE)
                .status(Status.SUCCESSFUL.name())
                .details("Schedule " + schedule.getId() + " retrieved successfully.")
                .eventType(EventType.SCHEDULE_READ)
                .build());

        return scheduleResponse(schedule);
    }

    @Transactional
    public List<ScheduleResponse> getAllSchedule(String day, LocalDate date, LocalTime time) {

        List<Schedule> scheduleList = scheduleRepository.findAllByDayOrDateOrTime(day, date, time);

        if (scheduleList.isEmpty())
            throw new RecordNotFoundException("no such record found");

        commonKafkaProducer.publishScheduleReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.SCHEDULE_READ.name())
                .entityName(EntityName.SCHEDULE)
                .status(Status.SUCCESSFUL.name())
                .details("All schedules retrieved successfully.")
                .eventType(EventType.SCHEDULE_READ)
                .build());

        return scheduleList.stream().map(this::scheduleResponse).toList();

    }

    @Transactional
    public void deleteSchedule(Long id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        scheduleRepository.deleteById(schedule.getId());

        commonKafkaProducer.publishScheduleDeletedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.SCHEDULE_DELETE.name())
                .entityName(EntityName.SCHEDULE)
                .status(Status.SUCCESSFUL.name())
                .details("Schedule " + schedule.getId() + " deleted successfully.")
                .eventType(EventType.SCHEDULE_DELETED)
                .build());
    }

    private ScheduleResponse scheduleResponse(Schedule schedule) {

        return new ScheduleResponse(
                schedule.getId(),
                schedule.getDoctor().getId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getStatus()
        );
    }
}
