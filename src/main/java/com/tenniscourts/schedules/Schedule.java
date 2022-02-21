package com.tenniscourts.schedules;

import com.tenniscourts.config.persistence.BaseEntity;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.tenniscourts.TennisCourt;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "reservations")
public class Schedule extends BaseEntity<Long> {

    @ManyToOne
    @NotNull
    private TennisCourt tennisCourt;

    @Column
    @NotNull
    private LocalDateTime startDateTime;

    @Column
    @NotNull
    private LocalDateTime endDateTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "reservation")
    private List<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }

        reservation.setSchedule(this);
        this.reservations.add(reservation);
    }
}
