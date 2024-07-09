package io.hhplus.concert.domain.concert;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
public class ConcertOption {
    private Long id;
    private Long concertId;
    private Integer price;
    private Integer seatQuantity;
    private Integer purchaseLimit;
    private LocalDateTime reserveFrom;
    private LocalDateTime reserveUntil;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Seat> seats;

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

    public void createSeat() {
        for (int number = 1; number <= seatQuantity; number++) {
            seats.add(new Seat(id, number));
        }
    }

    public boolean canReserve() {
        return LocalDateTime.now().isAfter(reserveFrom) && LocalDateTime.now().isBefore(reserveUntil);
    }

    public void checkPurchaseLimit(Integer request) {
        if (request > purchaseLimit) throw new IllegalArgumentException("최대 예매 가능 개수 초과");
    }
}
