package io.hhplus.concert.domain.token;

import io.hhplus.concert.application.token.UserTokenFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}
