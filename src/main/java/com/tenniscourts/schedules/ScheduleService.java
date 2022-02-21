package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    private ApplicationContext appContext;

    private final TennisCourtMapper tennisCourtMapper;


    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        TennisCourtService tennisCourtService = appContext.getBean(TennisCourtService.class);
        final TennisCourt tennisCourt = tennisCourtMapper.map(tennisCourtService.findTennisCourtById(createScheduleRequestDTO.getTennisCourtId()));
        final Schedule schedule = new Schedule();
        schedule.setStartDateTime(createScheduleRequestDTO.getStartDateTime());
        schedule.setEndDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1));
        schedule.setTennisCourt(tennisCourt);
        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleMapper.map(scheduleRepository.findByDates(startDate, endDate));
    }

    public ScheduleDTO updateScheduleReservation(Schedule schedule, Reservation reservation) {
        schedule.addReservation(reservation);
        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleMapper.map(scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found!")));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}
