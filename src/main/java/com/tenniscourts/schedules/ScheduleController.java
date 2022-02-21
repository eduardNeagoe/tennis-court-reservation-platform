package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping("/add")
    public ResponseEntity<Void> addScheduleTennisCourt(@Valid @RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDate endDate) {

        return ResponseEntity.ok(scheduleService.findSchedulesByDates(
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
