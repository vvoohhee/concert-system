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

    @Column(name = "reserve_from", nullable = false)
    private LocalDateTime reserveFrom;

    @Column(name = "reserve_until", nullable = false)
    private LocalDateTime reserveUntil;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "concert_id", referencedColumnName = "id")
    private Concert concert;

    public static int DEFAULT_SEAT_QUANTITY = 50;

    public static int DEFAULT_PURCHASE_LIMIT = 10;

    private void validateId(Long id) {
        if (Objects.isNull(id)) throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
    }

    public ConcertOption(Long id, Concert concert, Integer price, LocalDateTime reserveFrom, LocalDateTime reserveUntil) {
        validateId(id);
        validateId(concert.getId());

        this.id = id;
        this.concert = concert;
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = DEFAULT_PURCHASE_LIMIT;
        this.reserveFrom = reserveFrom;
        this.reserveUntil = reserveUntil;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    public ConcertOption(Long id, Concert concert, Integer price, Integer purchaseLimit, LocalDateTime reserveFrom, LocalDateTime reserveUntil) {
        validateId(concert.getId());

        this.id = id;
        this.concert = concert;
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = purchaseLimit;
        this.reserveFrom = reserveFrom;
        this.reserveUntil = reserveUntil;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    public boolean canReserve() {
        LocalDateTime now = LocalDateTime.now();
        return  now.isAfter(reserveFrom) && now.isBefore(reserveUntil);
    }

    public void checkPurchaseLimit(Integer request) {
        if (request > purchaseLimit) throw new CustomException(ErrorCode.RESERVATION_LIMIT_EXCEEDED);
    }
}
