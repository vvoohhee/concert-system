package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.OutboxStatus;
import io.hhplus.concert.common.util.ObjectStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "payment_outbox")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic")
    private String topic;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private OutboxStatus status;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    private final int initLimit = 5;

    public PaymentOutbox(String topic, Object message, String identifier) {
        this.topic = topic;
        this.status = OutboxStatus.INIT;
        this.message = ObjectStringConverter.toJson(message);
        this.identifier = identifier;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isRetryable() {
        return status == OutboxStatus.INIT && createdAt.isBefore(LocalDateTime.now().minusMinutes(initLimit));
    }

    public void markAsPublished() {
        this.status = OutboxStatus.PUBLISHED;
    }
}
