package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void userBooksOneReservationForATennisCourtAtGivenDateSchedule() {
        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO();
        createReservationRequestDTO.setGuestId(1L);
        createReservationRequestDTO.setScheduleId(1L);
        reservationService.bookReservation(createReservationRequestDTO);

        Reservation reservation = reservationRepository.findAll().get(0);
        assertEquals(createReservationRequestDTO.getGuestId(), reservation.getGuest().getId());
        assertEquals(createReservationRequestDTO.getScheduleId(), reservation.getSchedule().getId());
        assertEquals(ReservationStatus.READY_TO_PLAY, reservation.getReservationStatus());
        assertEquals(1, reservation.getSchedule().getReservations().size());
    }
}