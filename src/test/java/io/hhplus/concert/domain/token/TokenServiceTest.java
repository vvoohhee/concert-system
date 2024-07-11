package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test()
    @DisplayName("토큰발급_성공")
    void issueTest_성공() {
        // Given
        Long userId = 1L;
        Token token = new Token(userId);

        Token savedToken = new Token(userId);
        savedToken.setId(10L);  // ID 설정

        Optional<Long> firstPositionId = Optional.of(1L);

        // When
        when(tokenRepository.save(any(Token.class))).thenReturn(savedToken);
        when(tokenRepository.findFirstPositionId()).thenReturn(firstPositionId);

        // When
        Token issuedToken = tokenService.issue(userId);

        // Then
        assertNotNull(issuedToken);
        assertEquals(Math.toIntExact(savedToken.getId() - firstPositionId.get() + 1), issuedToken.getPosition());
        assertEquals(userId, issuedToken.getUserId());
    }

    @Test
    @DisplayName("토큰발급_실패_DB에서_토큰조회_실패")
    void issueTest_실패_DB에서_토큰조회_실패() {
        // Given
        Long userId = 1L;
        Token token = new Token(userId);

        Token savedToken = new Token(userId);
        savedToken.setId(10L);  // ID 설정

        // When
        when(tokenRepository.save(any(Token.class))).thenReturn(savedToken);
        when(tokenRepository.findFirstPositionId()).thenReturn(Optional.empty());

        // When Then
        assertThrows(NotFoundException.class, () -> tokenService.issue(1L));
    }

    @Test
    void findTest_실패_만료된_토큰을_조회() {
        // Given
        Long userId = 1L;
        Token token = new Token(userId);
        token.setId(10L);  // ID 설정
        token.setStatus(TokenStatusType.EXPIRED);

        // When
        when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));

        // Then
        assertThrows(CustomException.class, () -> tokenService.find(token.getToken()));
    }

    @Test
    void findTest_실패_토큰_조회_실패() {
        // Given
        Long userId = 1L;
        Token token = new Token(userId);
        token.setId(10L);  // ID 설정

        String tokenString = "없지롱ㅋㅋ";

        // When
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.of(token));

        // Then
        assertThrows(NotFoundException.class, () -> tokenService.find(tokenString));
    }
}
