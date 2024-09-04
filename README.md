# 콘서트 예매 시스템
항해 플러스 백엔드 과정에서 개발한 콘서트 예매 시스템입니다. <br>
TDD와 클린 레이어드 아키텍처 구조를 기반으로 서버를 설계하고 개발했습니다. <br>
대용량 데이터의 무결성과 성능을 보장하기 위해 동시성 제어, 트랜잭션 관리, 조회 성능 개선 방법을 배워 적용했습니다. <br>
대용량 트래픽에도 안정적으로 서비스를 운영하기 위해 Redis 자료구조를 활용한 대기열 시스템을 개발하고 Redis 캐싱, Kafka MQ 등의 기술을 사용했습니다. <br>

<br>

## 개발 문서

### 성능 개선을 위한 분석 문서

🔗 ![동시성 제어를 위한 락 전략](https://grand-spoonbill-ff4.notion.site/de84878f029341f4877b2bb2e67a25de) <br> 
🔗 ![쿼리 성능 개선 일지](https://grand-spoonbill-ff4.notion.site/294764528bd148ea9fedb8123e143301) <br>
🔗 ![k6 부하 테스트 분석](https://grand-spoonbill-ff4.notion.site/30bf98e0010249049b0239b53a6d7739) <br>
🔗 ![서비스 확장 설계](https://grand-spoonbill-ff4.notion.site/a0387d5cbf2448dfa0120b52e0b21a3f?pvs=74) <br>

### ERD
(ERD에 표현된 연관 관계는 논리적 연관관를 표현하기 위함이며 물리적으로 FK를 설정하지는 않습니다)
![ERD_최종](https://github.com/vvoohhee/hhplus_3rd_concert/assets/150509394/3c6b1ba9-a0be-4d84-ac30-1324aab15820)


### 시퀀스 다이어그램
```mermaid
sequenceDiagram
    title 콘서트 예약 전 대기열 다이어그램
    actor 사용자
    participant 대기열
    participant 처리열
        
    사용자 ->>+ 대기열 : 대기열 진입 요청

    대기열 ->>+ 사용자 : 토큰 발급
    
    loop 대기열 순서/상태 확인
        사용자 ->>+ 대기열 : 대기열 진입 요청
        대기열 ->>- 대기열 : 대기열 순서 확인
        대기열 ->>+ 사용자 : 사용자의 대기 상태, 순서 응답
    end
    
    alt 대기열 진입하고 5분 이상 경과
        대기열 ->>+ 대기열 : 토큰 삭제 처리
        대기열 ->>+ 사용자 : 대기 시간 만료 응답 
    end
    
    break 사용자의 순서가 되면 종료
        대기열 ->>+ 대기열 : 대기 상태 변경 (DONE)
        대기열 ->>+ 사용자 : 대기 상태, 순서 응답
    end

    사용자 ->>+ 처리열 : 처리열로 들어갈 수 있다.
    
```

```mermaid
sequenceDiagram
    title 유저 잔액 조회, 충전 다이어그램
    actor 사용자
    participant getapi as GET /api/balance
    participant postapi as POST /api/balance
    participant 잔액
    participant 유저

    사용자 ->>+ getapi : 잔액 조회 API 요청
    
    getapi ->>+ 잔액 : 사용자가 보유한 잔액 요청
    
    잔액 ->>+ 유저 : 유효한 유저인지 확인
    alt 일치하는 유저 정보가 없음
        유저 ->>+ 사용자 : 예외 처리
    end
    
    잔액 ->>+ getapi : 사용자가 보유한 잔액 응답

    getapi ->>+ 사용자 : 잔액 조회 API 응답
    
    alt 사용자가 잔액 조회 후 충전을 요청
        사용자 ->>+ postapi : 잔액 충전 API 요청
    end
    
    postapi ->>+ 잔액 : 사용자가 요청한 잔액 충전 요청

    잔액 ->>+ 유저 : 유효한 유저인지 확인
    alt 일치하는 유저 정보가 없음
        유저 ->>+ 사용자 : 예외 처리
    end
    
    잔액 ->>+ 잔액 : 잔액 충전 요청을 수행
    
    잔액 ->>+ postapi : 사용자가 요청한 잔액 충전 응답
    
    postapi ->>+ 사용자 : 잔액 충전 API 응답
```

```mermaid
sequenceDiagram
    title 콘서트 예약 가능 날짜, 좌석 조회 다이어그램
    actor 사용자
    participant 대기열
    participant concertapi as GET /api/concert
    participant seatapi as GET /api/concert/{optionId}/seat
    participant 콘서트
    participant 좌석
    
    사용자 ->>+ 대기열 : 대기열 진입 요청

    대기열 ->>+ 사용자 : 토큰 발급
    
    loop 대기열 순서/상태 확인
        사용자 ->>+ 대기열 : 대기열 진입 요청
        대기열 ->>- 대기열 : 대기열 순서 확인
        대기열 ->>+ 사용자 : 사용자의 대기 상태, 순서 응답
    end
    break 사용자의 순서가 되면 종료
        대기열 ->>+ 사용자 : 대기 상태, 순서 응답
    end

    사용자 ->>+ concertapi : 콘서트 날짜 조회 API 요청
    note right of 사용자 : 콘서트의 회차별 정보 조회

    concertapi ->>+ 콘서트 : 콘서트 정보 요청

    콘서트 ->>+ concertapi : 콘서트 정보 응답

    concertapi ->>+ 사용자 : 콘서트 날짜 조회 API 응답
    
    사용자 ->>+ seatapi : 콘서트 회차별 좌석 조회 API 요청
    note right of 사용자 : 콘서트 회차별 좌석 정보 조회
    
    seatapi ->>+ 좌석 : 좌석 정보와 상태 조회 요청
    
    좌석 ->>+ seatapi : 좌석 정보와 상태 조회 응답
    
    seatapi ->>+ 사용자 : 콘서트 회차별 좌석 조회 API 응답
    
```

```mermaid
sequenceDiagram
    title 좌석 예약, 결제 다이어그램
    actor 사용자
    participant reserveapi as GET /api/concert/reserve
    participant paymentapi as GET /api/concert/payment
    participant 유저
    participant 콘서트좌석
    participant 결제
    participant 티켓
    
        사용자 ->>+ reserveapi : 콘서트 예약 API 요청
        note right of 사용자 : 헤더에는 사용자의 토큰 정보가 포함됨

        reserveapi ->>+ 유저 : 토큰의 유저 정보 검증 요청
        
        alt 토큰으로 얻은 정보가 유효하지 않거나, 유효 시간 만료인 경우
            유저 ->>+ 사용자 : 예외 처리
        end
        
        reserveapi ->>+ 콘서트좌석 : 콘서트 좌석 예약 요청
        
        alt 콘서트 좌석 상태가 예약중
            콘서트좌석 ->>+ reserveapi : 예외 처리 ("이미 선택된 좌석입니다") 
        end
        
        콘서트좌석 ->>+ 콘서트좌석 : 상태값 "예약중"으로 변경
        note left of 콘서트좌석 : 락을 걸어 여러 사용자가 변경하지 못하도록 
        
        콘서트좌석 ->>+ reserveapi : 콘서트 좌석 예약 처리 후 응답
        
        reserveapi ->>+ 사용자 : 콘서트 예약 API 응답
        
        사용자 ->>+ paymentapi : 예약한 좌석에 결제 요청
        
        alt 사용자가 좌석 예약 후 정해진 시간 안에 결제 완료하지 않음
            결제 ->>+ 사용자 : 예외 처리 ("결제 시간을 초과했습니다.")
        end
        
        paymentapi ->>+ 결제 : 결제 요청
        
        결제 ->>+ 결제 : 결제 요청 처리
        
        결제 ->>+ 유저 : 유저의 잔액 업데이트
        
        유저 ->>+ 티켓 : 티켓 데이터 생성

        티켓 ->>+ 콘서트좌석 : 상태값 "예약완료"로 변경
        
        콘서트좌석 ->>+ paymentapi : 결제 요청 처리 후 응답
        
        paymentapi ->>+ 사용자 : 결제 요청 API 응답
        
```

<br><br>

<br><br>
### API 명세서
🔗 ![API 명세서](https://grand-spoonbill-ff4.notion.site/API-8f64568b50ab4b159201d2a863838528?pvs=74)

