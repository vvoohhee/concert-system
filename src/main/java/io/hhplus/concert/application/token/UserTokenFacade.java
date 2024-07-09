package io.hhplus.concert.application.token;

import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import io.hhplus.concert.presentation.token.dto.IssueTokenDto;
import io.hhplus.concert.presentation.token.dto.TokenStatusDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTokenFacade implements UserTokenService {
    private final TokenService tokenService;

    @Override
    @Transactional
    public IssueTokenDto.Response issueUserToken(IssueTokenDto.Request request) {
        Token token =  tokenService.issue(request.userId());
        return new IssueTokenDto.Response(token.getToken(), token.getStatus(), token.getExpiresAt());
    }

    @Override
    @Transactional
    public TokenStatusDto.Response findUserToken(String tokenString) {
        Token token = tokenService.find(tokenString);
        return new TokenStatusDto.Response(token.getToken(), token.getStatus(), token.getPosition(), token.getExpiresAt());
    }
}
