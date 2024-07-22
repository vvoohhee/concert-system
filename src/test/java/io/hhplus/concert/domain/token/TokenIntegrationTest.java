package io.hhplus.concert.domain.token;

import io.hhplus.concert.application.token.UserTokenFacade;
import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TokenService tokenService;

    @Test
    void 토큰발급_성공() {
        // Given
        Long userId = 1L;

        // When
        TokenInfo tokenInfo = userTokenFacade.issueUserToken(userId);

        // Then
        assertNotNull(tokenInfo);
        assertNotNull(tokenInfo.token());
        assertEquals(userId, tokenService.find(tokenInfo.token()).getUserId());
    }

    @Test
    void 토큰발급_실패_유저아이디_NULL() {
        // Given
        Long userId = null;

        // When - Then
        CustomException exception = assertThrows(CustomException.class, () -> userTokenFacade.issueUserToken(userId));
        assertEquals(exception.getErrorCode(), ErrorCode.ILLEGAL_ARGUMENT);
    }

    @Test
    void 토큰조회_성공() {
        // Given
        Long userId = 1L;
        TokenInfo issuedTokenInfo = userTokenFacade.issueUserToken(userId);

        // When
        TokenInfo foundTokenInfo = userTokenFacade.findUserToken(issuedTokenInfo.token());

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
                1);

        tokenRepository.save(token);

        // When
        boolean isAvailable = userTokenFacade.isAvailableToken(tokenString);

        // Then
        assertTrue(isAvailable);
    }
}
