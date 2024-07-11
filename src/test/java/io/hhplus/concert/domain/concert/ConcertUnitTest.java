package io.hhplus.concert.domain.concert;

import io.hhplus.concert.domain.concert.model.ConcertOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ConcertUnitTest {

    @Test
    @DisplayName("콘서트_예매_가능_시간_확인_성공")
    void Concert_콘서트_예매_가능_시간_확인_성공() {
        // given
        Long concertId = 1L;
        Long concertOptionId = 1L;
        Integer price = 10000;
        LocalDateTime reserveFrom = LocalDateTime.now().minusDays(1);
        LocalDateTime reserveUntil = LocalDateTime.now().plusDays(5);
        ConcertOption concertOption = new ConcertOption(concertOptionId, concertId, price, reserveFrom, reserveUntil);

        // when - then
        assertTrue(concertOption.canReserve());
    }

    @Test
    @DisplayName("콘서트_예매_가능_시간_확인_실패_예매일자_전")
    void Concert_콘서트_예매_가능_시간_확인_실패_예매일자_전() {
        // given
        Long concertId = 1L;
        Long concertOptionId = 1L;
        Integer price = 10000;
        LocalDateTime reserveFrom = LocalDateTime.now().plusDays(1);
        LocalDateTime reserveUntil = LocalDateTime.now().plusDays(5);
        ConcertOption concertOption = new ConcertOption(concertOptionId, concertId, price, reserveFrom, reserveUntil);

        // when - then
        assertFalse(concertOption.canReserve());
    }

    @Test
    @DisplayName("콘서트_예매_가능_시간_확인_실패_예매일자_후")
    void Concert_콘서트_예매_가능_시간_확인_실패_예매일자_후() {
        // given
        Long concertId = 1L;
        Long concertOptionId = 1L;
        Integer price = 10000;
        LocalDateTime reserveFrom = LocalDateTime.now().minusDays(5);
        LocalDateTime reserveUntil = LocalDateTime.now().minusDays(1);
        ConcertOption concertOption = new ConcertOption(concertOptionId, concertId, price, reserveFrom, reserveUntil);

        // when - then
        assertFalse(concertOption.canReserve());
    }

    @Test
    @DisplayName("콘서트_예매_최대_개수_초과")
    void Concert_콘서트_예매_최대_개수_초과() {
        // given
        Long concertId = 1L;
        Long concertOptionId = 1L;
        Integer price = 10000;
        Integer purchaseLimit = 5;
        LocalDateTime reserveFrom = LocalDateTime.now().minusDays(1);
        LocalDateTime reserveUntil = LocalDateTime.now().plusDays(5);

        ConcertOption concertOption = new ConcertOption(concertOptionId, concertId, price, purchaseLimit, reserveFrom, reserveUntil);

        // when - then
        assertThrows(IllegalArgumentException.class, () -> concertOption.checkPurchaseLimit(purchaseLimit + 1));
    }
}
