package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

    @Query("select s from Schedule s where s.startDateTime >= :startDate and s.endDateTime <= :endDate")
    List<Schedule> findByDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}