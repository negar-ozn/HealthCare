package com.healthcare.healthcare.repository;

import com.healthcare.healthcare.entitiy.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT s FROM SCHEDULE s WHERE s.weekday=:weekday OR s.date=:date OR s.time=:time")
    List<Schedule> findAllByDayOrDateOrTime(String weekday, LocalDate date, LocalTime time);
}
