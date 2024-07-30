package io.hhplus.concert.domain.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.model.Concert;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    private Concert concert;

    @BeforeEach()
    public void setUp() {
        this.concert = new Concert(1L, "워터밤양갱");
    }

    @Test
    public void findConcertsTest() {

        LocalDateTime reserveAt = LocalDateTime.of(2024, 9, 1, 20, 0);

        ConcertOption option1 = new ConcertOption(
                1L,
                concert,
                100000,
                10,
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 10, 12, 0)
        );


        when(concertRepository.findAvailableConcertOptions(reserveAt)).thenReturn(List.of(option1));

        List<ConcertOptionInfo> result = concertService.findConcerts(reserveAt);

        assertEquals(1, result.size());
        assertEquals(option1.getId(), result.get(0).id());
    }

    @Test
    public void findSeatsTest() {
        ConcertOption option1 = new ConcertOption(
                1L,
                concert,
                100000,
                10,
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 10, 12, 0)
        );

        Seat seat1 = new Seat(1L, option1.getId(), 1, ReservationStatusType.AVAILABLE);


        when(concertRepository.findSeatByConcertOptionId(option1.getId())).thenReturn(List.of(seat1));

        List<Seat> result = concertService.findSeats(1L);

        assertEquals(1, result.size());
        Seat info = result.get(0);
    }

    @Test
    public void reserveSeatsTest() {
        ConcertOption option1 = new ConcertOption(
                1L,
                concert,
                100000,
                10,
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 10, 12, 0)
        );

        Seat seat1 = new Seat(1L, option1.getId(), 1, ReservationStatusType.AVAILABLE);

        Reservation reservation = new Reservation(seat1.getId(), 1L, LocalDateTime.now());

        when(concertRepository.findSeatByIdIn(List.of(seat1.getId()))).thenReturn(List.of(seat1));
        when(concertRepository.saveReservation(any(Reservation.class))).thenReturn(reservation);

        boolean result = concertService.reserveSeats(List.of(seat1.getId()));

        assertTrue(result);
    }
}
