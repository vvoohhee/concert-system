package io.hhplus.concert.infrastructure.db.payment;

import io.hhplus.concert.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE payment", nativeQuery = true)
    void truncateTable();
}
