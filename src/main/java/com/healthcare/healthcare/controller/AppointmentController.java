package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.AppointmentDto;
import com.healthcare.healthcare.entitiy.Appointment;
import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.enums.AppointmentStatus;
import com.healthcare.healthcare.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments/newAppointment") // By Patient
    public ResponseEntity<Appointment> addAppointment(@RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.addAppointment(appointmentDto);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PutMapping("/appointments/editAppointment/{id}") // By Patient
    public ResponseEntity<Appointment> editAppointment(@PathVariable Long id, @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.editAppointment(id, appointmentDto);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/appointments/findAppointment/{id}") // By Patient, Staff, Doctor, Admin
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.getAppointment(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/appointments/allAppointments") // By Patient, Doctor, and Staff
    public ResponseEntity<AppointmentDto> allAppointments(@RequestParam(required = false) Doctor doctor,
                                                          @RequestParam(required = false) Patient patient,
                                                          @RequestParam(required = false) LocalDate appointmentDate,
                                                          @RequestParam(required = false) String nationalId) {
        AppointmentDto appointment = appointmentService.allAppointment(doctor, patient, appointmentDate, Long.valueOf(nationalId));
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PutMapping("/appointments/cancelAppointment/{id}") // By Patient
    public void cancelAppointment(@PathVariable Long id, @RequestParam(required = false) AppointmentStatus status) {
        appointmentService.cancelAppointment(id, status);
    }

    @PutMapping("/appointments/confirmAppointment/{id}") // By Patient
    public void confirmAppointment(@PathVariable Long id, @RequestParam(required = false) AppointmentStatus status) {
        appointmentService.confirmAppointment(id, status);
    }

    @PutMapping("/appointments/rescheduledAppointment/{id}") // By Patient
    public ResponseEntity<Appointment> rescheduledAppointment(@PathVariable Long id, @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.rescheduledAppointment(id, appointmentDto);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @DeleteMapping("/appointments/deleteAppointment/{id}")  // By Admin only!
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }

    @PutMapping("/appointments/checkIn") // By Staff
    public ResponseEntity<Appointment> checkInAppointment(@RequestParam(required = false) String nationalId) {
        Appointment appointment = appointmentService.checkInAppointment(Long.valueOf(nationalId));
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PutMapping("/appointments/noShow/{appointmentDate}") // By Staff
    public ResponseEntity<Appointment> noShowAppointment(@PathVariable LocalDate appointmentDate) {
        Appointment appointment = appointmentService.noShowAppointment(appointmentDate);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/appointments/todayAppointments/{date}") // By Staff
    public ResponseEntity<List<AppointmentDto>> todayAppointments(@PathVariable LocalDate date) {
        List<AppointmentDto> appointmentsList = appointmentService.todayAppointments(date);
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @PutMapping("/appointments/inProgress") // By Doctor
    public ResponseEntity<Appointment> inProgressAppointment(@RequestParam(required = false) String nationalId) {
        Appointment appointment = appointmentService.inProgressAppointment(Long.valueOf(nationalId));
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }
}
