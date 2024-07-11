package io.hhplus.concert.domain.concert;

import io.hhplus.concert.application.concert.UserConcertFacade;
import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.concert.model.Reservation;
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
public class UserConcertFacadeTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private ConcertService concertService;

    @InjectMocks
    private UserConcertFacade userConcertFacade;

    private String tokenString;

    @BeforeEach
    public void setUp() {
        // Mock tokenService의 동작 설정
        Token mockToken = new Token(1L); // 예시 토큰 객체
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
        assertThat(result).hasSize(2); // 예상되는 결과 크기로 확인
        // 추가적인 검증 로직을 여기에 추가할 수 있습니다.
    }

    @Test
    public void testReserveSeats() {
        // Given
        List<Long> seatIdList = Arrays.asList(1L, 2L);

        // When
        List<ReservationInfo> result = userConcertFacade.reserveSeats(seatIdList, tokenString);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // 예상되는 결과 크기로 확인
        // 추가적인 검증 로직을 여기에 추가할 수 있습니다.
    }
}
