package io.hhplus.concert.domain.concert;

import java.time.LocalDateTime;
import java.util.List;

public class Concert {
    private Long id;
    private String title;
    private List<ConcertOption> options;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
