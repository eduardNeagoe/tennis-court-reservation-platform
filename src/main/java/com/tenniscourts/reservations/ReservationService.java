package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.GuestMapper;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    private final GuestService guestService;

    private final GuestMapper guestMapper;

    private final ScheduleService scheduleService;

    private final ScheduleMapper scheduleMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        Reservation reservation = buildReservation(createReservationRequestDTO);
        Schedule schedule = findSchedule(createReservationRequestDTO);
        scheduleService.updateScheduleReservation(schedule, reservation);
        return getReservationDTO(createReservationRequestDTO, schedule);
    }

    private ReservationDTO getReservationDTO(CreateReservationRequestDTO createReservationRequestDTO, Schedule updatedSchedule) {
        return reservationMapper.map(updatedSchedule.getReservations().stream().filter(res ->
                res.getSchedule().getId().equals(createReservationRequestDTO.getScheduleId())
                        && res.getGuest().getId().equals(createReservationRequestDTO.getGuestId())).findFirst()
                        .orElseThrow(() -> new RuntimeException("Reservation not linked to schedule")));
    }

    private Reservation buildReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        Guest guest = guestMapper.map(findGuest(createReservationRequestDTO));
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setValue(new BigDecimal(100));
        reservation.setRefundValue(new BigDecimal(100));
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        return reservation;
    }

    private Schedule findSchedule(CreateReservationRequestDTO createReservationRequestDTO) {
        Long scheduleId = createReservationRequestDTO.getScheduleId();
        return scheduleMapper.map(scheduleService.findSchedule(scheduleId));
    }

    private GuestDTO findGuest(CreateReservationRequestDTO createReservationRequestDTO) {
        Long guestId = createReservationRequestDTO.getGuestId();
        return guestService.findById(guestId);
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        }

        return BigDecimal.ZERO;
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            updateReservation(previousReservation, new BigDecimal(0), ReservationStatus.READY_TO_PLAY);
        } else{
            previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        }
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }
}
