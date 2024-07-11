package io.hhplus.concert.domain.token;

import io.hhplus.concert.application.token.UserTokenFacade;
import io.hhplus.concert.common.enums.TokenStatusType;
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
    void issueUserToken_성공() {
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
    void findUserToken_성공() {
        // Given
        Long userId = 1L;
        TokenInfo issuedTokenInfo = userTokenFacade.issueUserToken(userId);

        // When
        TokenInfo foundTokenInfo = userTokenFacade.findUserToken(issuedTokenInfo.token());

        // Then
        assertNotNull(foundTokenInfo);
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
