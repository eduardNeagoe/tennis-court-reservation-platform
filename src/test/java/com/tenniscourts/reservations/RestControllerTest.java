package com.tenniscourts.reservations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceLoader resourceLoader;


    //Note: one test per controller; for the sake of efficiency

    @Test
    public void reservationController() throws Exception {

        mvc.perform(post("/reservation/book")
                        .content("{ \"guestId\": 1, \"scheduleId\": 1 }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String reservation = new String(Files.readAllBytes(resourceLoader.getResource("classpath:expected/reservation.json").getFile().toPath()));
        mvc.perform(get("/reservation/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(reservation));

        String cancelledReservation = new String(Files.readAllBytes(resourceLoader.getResource("classpath:expected/cancelledReservation.json").getFile().toPath()));
        mvc.perform(put("/reservation/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(cancelledReservation));

        mvc.perform(post("/reservation/book")
                        .content("{ \"guestId\": 2, \"scheduleId\": 2 }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(put("/reservation/reschedule/2/2"))
                .andExpect(status().isOk());

        mvc.perform(put("/reservation/reschedule/2/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void guestController() throws Exception {

        mvc.perform(post("/guest/add")
                        .content("{ \"name\": \"Elon\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(get("/guest/name/Elon"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"id\":4, \"name\": \"Elon\" }"));

        mvc.perform(put("/guest/update")
                        .content("{ \"id\": 4, \"name\": \"Elon Musk\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/guest/4"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"id\": 4, \"name\": \"Elon Musk\" }"));

        mvc.perform(delete("/guest/delete/4"))
                .andExpect(status().isOk());

        mvc.perform(get("/guest/name/Elon Musk"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void scheduleController() throws Exception {

        mvc.perform(post("/schedule/add")
                        .content("{ \"tennisCourtId\": 1, \"startDateTime\": \"2024-10-20T20:00\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String scheduleBetweenDates = new String(Files.readAllBytes(resourceLoader.getResource("classpath:expected/scheduleBetweenDates.json").getFile().toPath()));
        mvc.perform(get("/schedule/2021-10-20T20:00/2024-10-20T20:00"))
                .andExpect(status().isOk())
                .andExpect(content().json(scheduleBetweenDates));

        String schedule = new String(Files.readAllBytes(resourceLoader.getResource("classpath:expected/schedule.json").getFile().toPath()));
        mvc.perform(get("/schedule/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(schedule));
    }

}