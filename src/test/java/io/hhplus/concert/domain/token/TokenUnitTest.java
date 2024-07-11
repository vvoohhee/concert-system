package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static io.hhplus.concert.domain.token.Token.DURATION_MIN;
import static io.hhplus.concert.domain.token.Token.MAX_AVAILABLE_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenUnitTest {
    @Test
    @DisplayName("userId_Null로_토큰_생성")
    void issueTokenTest_userId_Null로_토큰_생성_테스트() {
        // given
        Long userId = null;

        // when - then
        when(new Token(userId)).thenThrow(new IllegalArgumentException());
    }

    @Test
    void testSetPosition() {
        Token token = new Token(10L);
        token.setId(10L);

        Long firstPositionId = 3L;

        // when
        token.setPosition(token.getId(), firstPositionId);

        // then
        assertEquals(8, token.getPosition());
    }
}
