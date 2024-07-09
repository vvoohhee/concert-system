package io.hhplus.concert.application.token;

import io.hhplus.concert.presentation.token.dto.IssueTokenDto;
import io.hhplus.concert.presentation.token.dto.TokenStatusDto;

public interface UserTokenService {
    IssueTokenDto.Response issueUserToken(IssueTokenDto.Request request);
    TokenStatusDto.Response findUserToken(String authorization);
}
