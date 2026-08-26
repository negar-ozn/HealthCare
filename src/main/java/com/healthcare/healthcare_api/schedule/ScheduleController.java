package com.healthcare.healthcare_api.schedule;

import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiDescription;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiResponseCode;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Tag(name = ApiDescription.SCHEDULE, description = ApiDescription.SCHEDULE_API)
@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = ApiSummary.NEW_SCHEDULE, description = ApiDescription.NEW_SCHEDULE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.SCHEDULE_CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.SCHEDULE_ALREADY_EXIST)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_CREATE')")
    @PostMapping("/schedule/newSchedule")
    public ResponseEntity<Schedule> addSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleService.addSchedule(scheduleRequest);
        return ResponseEntity.ok(schedule);
    }

    @Operation(summary = ApiSummary.EDIT_SCHEDULE, description = ApiDescription.EDIT_SCHEDULE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.SCHEDULE_MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.SCHEDULE_NOT_FOUND)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_UPDATE')")
    @PutMapping("/schedule/editSchedule/{id}")
    public ResponseEntity<Schedule> editSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleService.editSchedule(id, scheduleRequest);
        return ResponseEntity.ok(schedule);
    }

    @Operation(summary = ApiSummary.FIND_SCHEDULE, description = ApiDescription.FIND_SCHEDULE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.SCHEDULE_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.SCHEDULE_NOT_FOUND)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_READ')")
    @GetMapping("/schedule/getSchedule/{id}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable Long id) {
        ScheduleResponse schedule = scheduleService.getSchedule(id);
        return ResponseEntity.ok(schedule);
    }

    @Operation(summary = ApiSummary.ALL_SCHEDULE, description = ApiDescription.ALL_SCHEDULE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.SCHEDULES_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.SCHEDULES_NOT_FOUND)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_READ')")
    @GetMapping("/schedule/getSchedule")
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules(@RequestParam(required = false) String day,
                                                                  @RequestParam(required = false) LocalDate date,
                                                                  @RequestParam(required = false) LocalTime time) {
        List<ScheduleResponse> schedule = scheduleService.getAllSchedule(day, date, time);
        return ResponseEntity.ok(schedule);
    }

    @Operation(summary = ApiSummary.DELETE_SCHEDULE, description = ApiDescription.DELETE_SCHEDULE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.SCHEDULE_DELETED_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.SCHEDULE_NOT_FOUND)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_DELETE')")
    @DeleteMapping("/schedule/deleteSchedule/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
