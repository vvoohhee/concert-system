package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.common.util.RestPage;
import io.hhplus.concert.domain.concert.model.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertsDto {
    public record Response(
            Long id,
            String concertTitle,
            LocalDateTime reserveFrom,
            LocalDateTime reserveUntil
    ) {
        public static Page<Response> of(Page<Concert> page) {
            List<Response> responses = page.getContent().stream()
                    .map(concert ->
                            new Response(concert.getId(),
                                    concert.getTitle(),
                                    concert.getReserveFrom(),
                                    concert.getReserveUntil()))
                    .toList();

            return new RestPage<>(responses, page.getPageable());
        }
    }
}
