package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.ScheduleDto;
import com.healthcare.healthcare.entitiy.Schedule;
import com.healthcare.healthcare.exception.NoSuchRecordFoundException;
import com.healthcare.healthcare.exception.ScheduleAlreadyExistsException;
import com.healthcare.healthcare.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    //All services run by Staff!

    public Schedule addSchedule(ScheduleDto scheduleDto) {
        Optional<Schedule> optional = scheduleRepository.findById(scheduleDto.getId());
        if(optional.isPresent()) {
            try {
                throw new ScheduleAlreadyExistsException("schedule already exists");
            } catch (ScheduleAlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        }
        Schedule schedule = new Schedule();
        schedule.setWeekday(scheduleDto.getDay());
        schedule.setDate(scheduleDto.getDate());
        schedule.setTime(scheduleDto.getTime());
        schedule.setDoctor(scheduleDto.getDoctor());
        schedule.setStatus(scheduleDto.getStatus());
        return scheduleRepository.save(schedule);
    }

    public Schedule editSchedule(Long id, ScheduleDto scheduleDto) {
        Optional<Schedule> optional = scheduleRepository.findById(id);
        if(optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Schedule schedule = optional.get();
        schedule.setId(id);
        schedule.setWeekday(scheduleDto.getDay());
        schedule.setDate(scheduleDto.getDate());
        schedule.setTime(scheduleDto.getTime());
        schedule.setDoctor(scheduleDto.getDoctor());
        schedule.setStatus(scheduleDto.getStatus());
        return scheduleRepository.save(schedule);
    }

    public ScheduleDto getSchedule(Long id) {
        Optional<Schedule> optional = scheduleRepository.findById(id);
        if(optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Schedule schedule = optional.get();
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setId(id);
        scheduleDto.setDay(schedule.getWeekday());
        scheduleDto.setDate(schedule.getDate());
        scheduleDto.setTime(schedule.getTime());
        scheduleDto.setDoctor(schedule.getDoctor());
        scheduleDto.setStatus(schedule.getStatus());
        return scheduleDto;
    }

    public List<ScheduleDto> getAllSchedule(String day, LocalDate date, LocalTime time) {
        List<Schedule> scheduleList = scheduleRepository.findAllByDayOrDateOrTime(day, date, time);
        if(scheduleList.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        ScheduleDto scheduleDto = new ScheduleDto();
        for (Schedule schedule : scheduleList) {
            schedule.setId(scheduleDto.getId());
            schedule.setWeekday(scheduleDto.getDay());
            schedule.setDate(scheduleDto.getDate());
            schedule.setTime(scheduleDto.getTime());
            schedule.setDoctor(scheduleDto.getDoctor());
            schedule.setStatus(scheduleDto.getStatus());
            scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    public void deleteSchedule(Long id) {
        Optional<Schedule> optional = scheduleRepository.findById(id);
        if(optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Schedule schedule = optional.get();
        scheduleRepository.deleteById(schedule.getId());
    }
}
