package io.hhplus.concert.domain.concert;

import com.zaxxer.hikari.HikariDataSource;
import io.hhplus.concert.application.concert.UserConcertFacade;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.concert.model.Concert;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.domain.token.TokenService;
import io.hhplus.concert.infrastructure.concert.ConcertJpaRepository;
import io.hhplus.concert.infrastructure.concert.ConcertOptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ConcertIntegrationTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertOptionJpaRepository concertOptionJpaRepository;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private UserConcertFacade userConcertFacade;

    private String tokenString;

    private Concert concert;

    @BeforeEach
    public void setUp() {
//        Long userId = 1L;
//        Token token = tokenService.issueWaitingToken(userId);
//        tokenString = token.getToken();
//
//        Concert concert = new Concert(null, "워터밤양갱");
//        concert = concertJpaRepository.save(concert);
//        this.concert = concert;
//
//        ConcertOption option1 = new ConcertOption(
//                null,
//                concert,
//                10000,
//                10,
//                LocalDateTime.of(2024, 9, 1, 0, 0),
//                LocalDateTime.of(2024, 9, 10, 12, 0)
//        );
//        ConcertOption option2 = new ConcertOption(
//                null,
//                concert,
//                10000,
//                10,
//                LocalDateTime.of(2024, 10, 1, 0, 0),
//                LocalDateTime.of(2024, 10, 10, 12, 0)
//        );
//
//        concertOptionJpaRepository.saveAll(List.of(option1, option2));
    }

    @Test
    public void 콘서트_대용량삽입용_CSV생성() {
        List<Concert> concerts = LongStream.rangeClosed(2000001L, 4000000L)
                .mapToObj(number -> {
                    LocalDateTime reserveFrom = LocalDateTime.now().minusSeconds(new Random().nextInt(864000000) + 1);
                    LocalDateTime reserveUntil = reserveFrom.plusDays(new Random().nextInt(6, 30));
                    return new Concert(number + "번째 콘서트", reserveFrom, reserveUntil);
                })
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("concert.csv"))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // CSV 헤더 작성
            writer.write("title,reserveFrom,reserveUntil");
            writer.newLine();

            for (Concert concert : concerts) {
                writer.write(concert.getTitle() + "," +
                        concert.getReserveFrom().format(formatter) + "," +
                        concert.getReserveUntil().format(formatter));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void 콘서트옵션_대용량삽입용_CSV생성() {
        LocalDateTime startAt = LocalDateTime.of(2024, 12, 24, 14, 0);
        LocalDateTime endAt = startAt.plusHours(3);

        List<ConcertOption> concerts = LongStream.rangeClosed(1L, 4000000L)
                .mapToObj(number -> new ConcertOption(number, 100000, startAt, endAt))
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("concertOption2.csv"))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // CSV 헤더 작성
            writer.write("concertId,price,purchaseLimit,seatQuantity,startAt,endAt");
            writer.newLine();

            for (ConcertOption option : concerts) {
                writer.write(
                        option.getConcertId() + "," +
                                option.getPrice() + "," +
                                option.getPurchaseLimit() + "," +
                                option.getSeatQuantity() + "," +
                                option.getStartAt().format(formatter) + "," +
                                option.getEndAt().format(formatter));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void 좌석_대용량삽입용_CSV생성() {
        for (int i = 1; i <= 120; i++) {
            // i = 10만
            List<Seat> seats = new ArrayList<>();
            LongStream.rangeClosed((i-1) * 100000 + 1L, i * 100000L) //10만개씩 끊어서
                    .forEach(number -> {
                        for (int j = 1; j <= 50; j++) seats.add(new Seat(number, j));
                    });

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Seat" + i + ".csv"))) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // CSV 헤더 작성
                writer.write("concertOptionId,number,status");
                writer.newLine();

                for (Seat seat : seats) {
                    writer.write(
                            seat.getConcertOptionId() + "," +
                                    seat.getNumber() + "," +
                                    seat.getStatus());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Test
    public void 콘서트_1000개_생성() {
        List<Concert> concerts = LongStream.rangeClosed(1L, 1000L)
                .mapToObj(number -> new Concert(number + "번째 콘서트"))
                .toList();
        concerts = concertJpaRepository.saveAll(concerts);

        List<ConcertOption> options = new ArrayList<>();
        for (Concert concert : concerts) {
            ConcertOption option1 = new ConcertOption(
                    null,
                    concert,
                    10000,
                    10
            );
            ConcertOption option2 = new ConcertOption(
                    null,
                    concert,
                    10000,
                    10
            );
            options.add(option1);
            options.add(option2);
        }
    }

//        List<ConcertOption> options = new ArrayList<>();
//        for (Concert concert : concerts) {
//            ConcertOption option1 = new ConcertOption(
//                    null,
//                    concert,
//                    10000,
//                    10,
//                    LocalDateTime.of(2024, 9, 1, 0, 0),
//                    LocalDateTime.of(2024, 10, 1, 23, 59)
//            );
//            ConcertOption option2 = new ConcertOption(
//                    null,
//                    concert,
//                    10000,
//                    10,
//                    LocalDateTime.of(2024, 9, 1, 0, 0),
//                    LocalDateTime.of(2024, 10, 1, 23, 59)
//            );
//            options.add(option1);
//            options.add(option2);
//        }
//        concertOptionJpaRepository.saveAll(options);
//    }

    @Test
    public void 콘서트조회_성공() {
        // Given
        LocalDateTime reserveAt = LocalDateTime.of(2024, 9, 10, 12, 0);

        // When
        Page<Concert> result = userConcertFacade.findConcerts(reserveAt);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    public void testFindSeats() {
        // Given
        Long concertOptionId = 1L;

        // When
        List<SeatInfo> result = userConcertFacade.findSeats(concertOptionId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    public void testReserveSeats() {
        // Given
        List<Long> seatIdList = Arrays.asList(1L, 2L);

        // When
        List<ReservationInfo> result = userConcertFacade.reserveSeats(seatIdList, tokenString);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }
}
