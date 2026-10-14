package com.healthcare.healthcare_api.schedule;

import com.healthcare.healthcare_api.shared.enums.AppointmentStatus;
import com.healthcare.healthcare_api.shared.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = """
            SELECT s
            FROM Schedule s
            WHERE s.weekday=:weekday
                OR s.date=:date
                OR s.startTime=:startTime
            """)
    List<Schedule> findAllByDayOrDateOrTime(String weekday, LocalDate date, LocalTime startTime);

    @Query("""
                SELECT s
                FROM Schedule s
                WHERE s.doctor.id = :doctorId
                  AND s.date = :date
                  AND s.status = :scheduleStatus
                  AND NOT EXISTS (
                      SELECT a.id
                      FROM Appointment a
                      WHERE a.schedule.id = s.id
                        AND a.status IN :activeStatusList
                  )
            """)
    List<Schedule> findAvailableSlots(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("scheduleStatus") ScheduleStatus scheduleStatus,
            @Param("activeStatusList") Collection<AppointmentStatus> activeStatusList
    );
}
