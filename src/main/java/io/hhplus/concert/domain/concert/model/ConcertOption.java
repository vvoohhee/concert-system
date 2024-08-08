package io.hhplus.concert.domain.concert.model;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "concert_option")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ConcertOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "seat_quantity", nullable = false)
    private Integer seatQuantity;

    @Column(name = "purchase_limit")
    private Integer purchaseLimit;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "created_at",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "concert_id", nullable = false)
    private Long concertId;

    public static int DEFAULT_SEAT_QUANTITY = 50;

    public static int DEFAULT_PURCHASE_LIMIT = 10;

    private void validateId(Long id) {
        if (Objects.isNull(id)) throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
    }

    public ConcertOption(Long id, Concert concert, Integer price) {
        validateId(id);
        validateId(concert.getId());

        this.id = id;
        this.concertId = concert.getId();
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = DEFAULT_PURCHASE_LIMIT;
    }

    public ConcertOption(Long id, Concert concert, Integer price, Integer purchaseLimit) {
        validateId(concert.getId());

        this.id = id;
        this.concertId = concert.getId();
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = purchaseLimit;
    }

    public ConcertOption(Long concertId, Integer price, LocalDateTime startAt, LocalDateTime endAt) {
        this.concertId = concertId;
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = DEFAULT_PURCHASE_LIMIT;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void checkPurchaseLimit(Integer request) {
        if (request > purchaseLimit) throw new CustomException(ErrorCode.RESERVATION_LIMIT_EXCEEDED);
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }
}
