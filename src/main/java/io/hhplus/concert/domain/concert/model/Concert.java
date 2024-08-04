package io.hhplus.concert.domain.concert.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.hhplus.concert.common.util.LocalDateTimeDeserializer;
import io.hhplus.concert.common.util.LocalDateTimeSerializer;
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

    @Column(name = "created_at",
            nullable = false,
            updatable = false)
    private Long createdAt;

    @Column(name = "updated_at",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Long updatedAt;

    public Concert(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Concert(String title) {
        this.title = title;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }
}
