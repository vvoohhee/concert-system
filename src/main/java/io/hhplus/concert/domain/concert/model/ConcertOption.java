package io.hhplus.concert.domain.concert.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "concert_option")
@NoArgsConstructor
@Getter
public class ConcertOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "concert_id", nullable = false)
    private Long concertId;

    @Column(name = "price")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", referencedColumnName = "id", nullable = false)
    private Concert concert;

    public static int DEFAULT_SEAT_QUANTITY = 50;

    public static int DEFAULT_PURCHASE_LIMIT = 10;

    private void validateId(Long id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("유효하지 않은 ID");
    }

    public ConcertOption(Long id, Long concertId, Integer price, LocalDateTime reserveFrom, LocalDateTime reserveUntil) {
        validateId(id);
        validateId(concertId);

        this.id = id;
        this.concertId = concertId;
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = DEFAULT_PURCHASE_LIMIT;
        this.reserveFrom = reserveFrom;
        this.reserveUntil = reserveUntil;
    }

    public ConcertOption(Long concertId, Integer price, LocalDateTime reserveFrom, LocalDateTime reserveUntil) {
        validateId(concertId);

        this.concertId = concertId;
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = DEFAULT_PURCHASE_LIMIT;
        this.reserveFrom = reserveFrom;
        this.reserveUntil = reserveUntil;
    }

    public ConcertOption(Long id, Long concertId, Integer price, Integer purchaseLimit, LocalDateTime reserveFrom, LocalDateTime reserveUntil) {
        validateId(id);
        validateId(concertId);

        this.id = id;
        this.concertId = concertId;
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = purchaseLimit;
        this.reserveFrom = reserveFrom;
        this.reserveUntil = reserveUntil;
    }

    public ConcertOption(Long concertId, Integer price, Integer purchaseLimit, LocalDateTime reserveFrom, LocalDateTime reserveUntil) {
        validateId(concertId);

        this.concertId = concertId;
        this.price = price;
        this.seatQuantity = DEFAULT_SEAT_QUANTITY;
        this.purchaseLimit = purchaseLimit;
        this.reserveFrom = reserveFrom;
        this.reserveUntil = reserveUntil;
    }

    public boolean canReserve() {
        return LocalDateTime.now().isAfter(reserveFrom) && LocalDateTime.now().isBefore(reserveUntil);
    }

    public void checkPurchaseLimit(Integer request) {
        if (request > purchaseLimit) throw new IllegalArgumentException("최대 예매 가능 개수 초과");
    }
}
