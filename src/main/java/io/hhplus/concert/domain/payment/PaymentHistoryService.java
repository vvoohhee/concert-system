package io.hhplus.concert.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHistoryService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void savePaymentHistories(List<PaymentHistoryCommand> command) {
        List<Payment> payments = command
                .stream()
                .map(c -> new Payment(c.getTicketId(), c.getPrice(), c.getStatus()))
                .toList();

        try {
            paymentRepository.savePayments(payments);
        } catch (Exception e) {
            ObjectMapper objectMapper = new ObjectMapper();
            log.error("[결제API][결제히스토리생성] 결제 정보를 DB에 저장하는 과정에서 알 수 없는 에러 발생");
            try {
                // 실패한 데이터를 로그로 확인할 수 있도록 JSON String으로 변경 후 로그에 기록
                log.error("[결제API][결제히스토리생성] 요청 데이터 : {}", objectMapper.writeValueAsString(payments));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Transactional
    public void deleteAllPaymentHistories() {
        paymentRepository.deleteAllPaymentHistories();
    }
}
