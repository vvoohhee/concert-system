## 좌석 예약, 결제 다이어그램

---

- 대기열에 들어온 사용자는 콘서트 회차별 좌석을 1개 이상 예매할 수 있습니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 (예시 : 5분)간 임시 배정됩니다.
  <br> 임시배정된 상태라면 다른 사용자는 예약할 수 없어야 합니다.
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 합니다.
- 예약 요청이 성공적으로 이루어지면, 결제 API를 요청합니다.
- 결제가 완료되면 해당 좌석의 소유권(티켓)을 유저에게 배정하고 대기열 토큰을 만료시킵니다.

<br><br>

```mermaid
sequenceDiagram
    title 콘서트 예약 가능 날짜, 좌석 조회 다이어그램
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