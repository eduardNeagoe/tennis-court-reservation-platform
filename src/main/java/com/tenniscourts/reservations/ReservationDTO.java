package com.tenniscourts.reservations;

import com.tenniscourts.schedules.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ReservationDTO {

    private Long id;

    private ScheduleDTO schedule;

    private String reservationStatus;

    private ReservationDTO previousReservation;

    private BigDecimal refundValue;

    private BigDecimal value;

    @NotNull
    private Long scheduledId;

    @NotNull
    private Long guestId;
}
