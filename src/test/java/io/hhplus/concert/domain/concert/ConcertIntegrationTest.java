package io.hhplus.concert.domain.concert;

import io.hhplus.concert.application.concert.UserConcertFacade;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertIntegrationTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private ConcertService concertService;

    @InjectMocks
    private UserConcertFacade userConcertFacade;

    private String tokenString;

    @BeforeEach
    public void setUp() {
        Token mockToken = new Token(1L);
        tokenString = mockToken.toString();
        when(tokenService.find(mockToken.getToken())).thenReturn(mockToken);
    }

    @Test
    public void findConcertsTest() {
        // Given
        LocalDateTime reserveAt = LocalDateTime.of(2024, 8, 1, 12, 0);

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
