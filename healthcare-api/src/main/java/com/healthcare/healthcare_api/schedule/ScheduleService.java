package com.healthcare.healthcare_api.schedule;

import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.NoSuchRecordFoundException;
import com.healthcare.healthcare_api.shared.exception.ScheduleAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    //All services run by Receptionist!

    public Schedule addSchedule(ScheduleRequest scheduleRequest) {

        scheduleRepository.findById(scheduleRequest.id())
                .orElseThrow(() -> new ScheduleAlreadyExistsException("schedule already exists"));
        Schedule schedule = Schedule.builder()
                .weekday(scheduleRequest.weekday())
                .date(scheduleRequest.date())
                .time(scheduleRequest.time())
                .status(Status.AVAILABLE)
                .doctor(scheduleRequest.doctor())
//                .version()
                .build();
        return scheduleRepository.save(schedule);
    }

    public Schedule editSchedule(Long id, ScheduleRequest scheduleRequest) {

        Schedule scheduleOp = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));

        Schedule schedule = Schedule.builder()
                .id(scheduleOp.getId())
                .weekday(scheduleRequest.weekday())
                .date(scheduleRequest.date())
                .time(scheduleRequest.time())
                .status(scheduleRequest.status())
//                .version()
                .doctor(scheduleRequest.doctor())
                .build();
        return scheduleRepository.save(schedule);
    }

    public ScheduleResponse getSchedule(Long id) {

        Schedule scheduleOp = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        return ScheduleResponse.builder()
                .doctor(scheduleOp.getDoctor())
                .weekday(scheduleOp.getWeekday())
                .date(scheduleOp.getDate())
                .time(scheduleOp.getTime())
                .status(scheduleOp.getStatus())
                .build();
    }

    public List<ScheduleResponse> getAllSchedule(String day, LocalDate date, LocalTime time) {

        List<Schedule> scheduleList = scheduleRepository.findAllByDayOrDateOrTime(day, date, time);
        if (scheduleList.isEmpty())
            throw new NoSuchRecordFoundException("no such record found");

        return scheduleList.stream().map(schedule -> ScheduleResponse.builder()
                .doctor(schedule.getDoctor())
                .weekday(schedule.getWeekday())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .status(schedule.getStatus())
                .build()).toList();

    }

    public void deleteSchedule(Long id) {

        Schedule scheduleOp = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        scheduleRepository.deleteById(scheduleOp.getId());
    }
}
