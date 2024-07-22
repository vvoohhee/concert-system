package io.hhplus.concert.domain.concert;

import io.hhplus.concert.application.concert.UserConcertFacade;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.concert.model.Concert;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import io.hhplus.concert.infrastructure.concert.ConcertJpaRepository;
import io.hhplus.concert.infrastructure.concert.ConcertOptionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ConcertIntegrationTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertOptionJpaRepository concertOptionJpaRepository;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private UserConcertFacade userConcertFacade;

    private String tokenString;

    private Concert concert;

    @BeforeEach
    public void setUp() {
        Long userId = 1L;
        Token token = tokenService.issue(userId);
        tokenString = token.getToken();

        Concert concert = new Concert(null, "워터밤양갱");
        concert = concertJpaRepository.save(concert);
        this.concert = concert;

        ConcertOption option1 = new ConcertOption(
                null,
                concert,
                10000,
                10,
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 10, 12, 0)
        );
        ConcertOption option2 = new ConcertOption(
                null,
                concert,
                10000,
                10,
                LocalDateTime.of(2024, 10, 1, 0, 0),
                LocalDateTime.of(2024, 10, 10, 12, 0)
        );

        concertOptionJpaRepository.saveAll(List.of(option1, option2));
    }

    @Test
    public void 콘서트조회_성공() {
        // Given
        LocalDateTime reserveAt = LocalDateTime.of(2024, 9, 1, 12, 0);

        // When
        List<ConcertOptionInfo> result = userConcertFacade.findConcerts(reserveAt);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    public void testFindSeats() {
        // Given
        Long concertOptionId = 1L;

        // When
        List<SeatInfo> result = userConcertFacade.findSeats(concertOptionId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    public void testReserveSeats() {
        // Given
        List<Long> seatIdList = Arrays.asList(1L, 2L);

        // When
        List<ReservationInfo> result = userConcertFacade.reserveSeats(seatIdList, tokenString);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }
}
