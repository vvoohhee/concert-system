package io.hhplus.concert.domain.token;

import io.hhplus.concert.application.token.UserTokenFacade;
import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.user.User;
import io.hhplus.concert.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TokenIntegrationTest {

    @Autowired
    private UserTokenFacade userTokenFacade;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Test
    void 토큰발급_성공() {
        // Given
        User user = userRepository.saveUser(new User(1L));
        Long userId = user.getId();

        // When
        TokenInfo tokenInfo = userTokenFacade.issueWaitingToken(userId);

        // Then
        assertNotNull(tokenInfo);
        assertNotNull(tokenInfo.token());
        assertEquals(userId, tokenService.findWaitingToken(tokenInfo.token()).getUserId());
    }

    @Test
    void 토큰발급_성공_50건() {
        // Given
        for (long i = 1; i <= 50; i++) {
            userRepository.saveUser(new User(i));
        }

        // When
        for (long userId = 1; userId <= 50; userId++) {
            TokenInfo tokenInfo = userTokenFacade.issueWaitingToken(userId);

            // Then
            assertNotNull(tokenInfo);
            assertNotNull(tokenInfo.token());
            assertEquals(userId, tokenService.findWaitingToken(tokenInfo.token()).getUserId());
        }
    }

    @Test
    void 토큰발급_실패_유저아이디_NULL() {
        // Given
        Long userId = null;

        // When - Then
        CustomException exception = assertThrows(CustomException.class, () -> userTokenFacade.issueWaitingToken(userId));
        assertEquals(exception.getErrorCode(), ErrorCode.ILLEGAL_ARGUMENT);
    }

    @Test
    void 토큰조회_성공() {
        // Given
        Long userId = 1L;
        TokenInfo issuedTokenInfo = userTokenFacade.issueWaitingToken(userId);

        // When
        TokenInfo foundTokenInfo = userTokenFacade.findWaitingToken(issuedTokenInfo.token());

        // Then
        assertEquals(issuedTokenInfo.token(), foundTokenInfo.token());
        assertEquals(issuedTokenInfo.status(), foundTokenInfo.status());
        assertEquals(issuedTokenInfo.position(), foundTokenInfo.position());
    }

    @Test
    void isAvailableToken_성공() {
        // Given
        String tokenString = UUID.randomUUID().toString();
        Token token = new Token(1L,
                1L,
                tokenString,
                TokenStatusType.AVAILABLE,
                LocalDateTime.now().plusMinutes(30),
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5),
                null,
                1L);

        tokenRepository.issueWaitingToken(token);

        // When
        boolean isAvailable = userTokenFacade.isAvailableToken(tokenString);

        // Then
        assertTrue(isAvailable);
    }
}
