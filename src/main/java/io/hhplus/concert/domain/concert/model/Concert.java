package io.hhplus.concert.domain.concert.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert")
@Getter
@NoArgsConstructor
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "reserve_from", nullable = false)
    private LocalDateTime reserveFrom;

    @Column(name = "reserve_until", nullable = false)
    private LocalDateTime reserveUntil;


    @Column(name = "created_at",
            nullable = false,
            updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public Concert(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Concert(String title) {
        this.title = title;
    }

    public Concert(String title, LocalDateTime reserveFrom, LocalDateTime reserveUntil) {
        this.title = title;
        this.reserveFrom = reserveFrom;
        this.reserveUntil = reserveUntil;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }
}
