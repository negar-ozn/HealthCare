package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.ScheduleDto;
import com.healthcare.healthcare.entitiy.Schedule;
import com.healthcare.healthcare.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class SchedulesController {

    private final ScheduleService scheduleService;

    public SchedulesController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    //All services run by Staff!

    @PostMapping("/schedule/newSchedule")
    public ResponseEntity<Schedule> addSchedule(@Valid @RequestBody ScheduleDto scheduleDto) {
        Schedule schedule = scheduleService.addSchedule(scheduleDto);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PutMapping("/schedule/editSchedule/{id}")
    public ResponseEntity<Schedule> editSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleDto scheduleDto) {
        Schedule schedule = scheduleService.editSchedule(id, scheduleDto);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("/schedule/getSchedule/{id}")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable Long id) {
        ScheduleDto schedule = scheduleService.getSchedule(id);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("/schedule/getSchedule")
    public ResponseEntity<List<ScheduleDto>> getAllSchedules(@RequestParam(required = false) String day,
                                                             @RequestParam(required = false) LocalDate date,
                                                             @RequestParam(required = false) LocalTime time) {
        List<ScheduleDto> schedule = scheduleService.getAllSchedule(day, date, time);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @DeleteMapping("/schedule/deleteSchedule/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }
}
