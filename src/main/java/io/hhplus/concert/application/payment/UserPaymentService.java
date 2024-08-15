package io.hhplus.concert.application.payment;

import io.hhplus.concert.domain.payment.dto.TicketInfo;

import java.util.List;

public interface UserPaymentService {
    List<TicketInfo> billing(String authorization);
    void retryInitMessages();

    void markOutboxAsPublished(String identifier);
}
