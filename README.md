# hhplus-tdd-java-2week (수강 신청 관리 구현)

### 구현 과제 
- 특강 신청 API 
- 특강 신청 여부 확인 API -> 신청 완료 강의 목록 리턴
- 특강 신청 가능 목록 조회 API 
### 고려할 사항 : 클린코드 , 클린아키텍쳐 , 동시성 , TDD 


<br>

### TOOL & OTHER 

![spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![mysql](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
, junit , JPA , h2(테스트용) 
<br>
<h2> 요구사항 분석 </h2>

<details> 
<summary>
   <b>명시된 요구사항</b>
</summary>
  <br> 
1. 사용자는 userId 로 식별함 OK<br> 
2. 강의 정원은 30명 OK<br> 
3. 정원 초과시 수강신청 실패 OK<br>
4. 신청 정보는 히스토리 저장 OK<br>
5. 특정날짜/특정강의 신청 후 동일 건 신청 불가 OK<br>
6. 특강 신청 성공한 내역 리스트 응답 OK <br>
7. 신청 가능한 특강 목록 리스트 응답 OK <br> 
</details>

<details> 
<summary>
   <b>추가 도출한 사항</b>
</summary>
<br> 
1. 컬럼 추가시 강의 정원 n명으로 구성 가능 OK <br> 
2. ... <br>
</details>

<br>

<h2>ERD (데이터 모델링 & DBML)  </h2> <br>

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/be7a4212-840d-4b0e-bae4-ab13220f6765)

<a href="https://dbdiagram.io/d/6677a8115a764b3c722aa3d5">**링크**</a> 

<details>
   <summary>DBML 코드</summary>

Table user {  <br>
  user_id integer [pk]  <br>
  user_name varchar(50) [not null]  <br>
  phone varchar(15) [not null, unique]   <br>
  created_at timestamp [not null]  <br>
}  <br>
  <br>
Table lecture {  <br>
  lecture_id integer [pk]  <br>
  lecture_name varchar(200) [not null]   <br>
  max_capacity integer [default: 30 , not null]  <br>
  lecture_info varchar(4000) [not null]  <br>
  created_at timestamp [not null]  <br>
}  <br>
  <br>
Table lecture_schedule {  <br>
  lecture_id integer [pk]  <br>
  lecture_dy varchar(8) [pk]  <br>
  start_date date [not null]  <br>
  end_date date [not null]  <br>
  enroll_stmt varchar(10) [not null] <br>
  created_at timestamp [not null]  <br>
}  <br>
  <br>
Table enrollment {  <br>
  user_id integer [pk]  <br>
  lecture_id integer [pk]  <br>
  lecture_dy varchar(8) [pk]  <br>
  created_at timestamp [not null]  <br>
}  <br>

table enrollment_history {  <br>
  seq integer [pk]  <br>
  user_id integer [not null]  <br>
  lecture_id integer [not null]  <br>
  lecture_dy varchar(8) [not null]  <br>
  enroll_stmt varchar(10) [not null]  <br>
  created_at timestamp [not null]  <br>
}  <br>

Ref: user.user_id < enrollment.user_id [delete : cascade]  <br>
Ref: enrollment.user_id < enrollment_history.user_id // many-to-one   <br>
Ref: lecture.lecture_id < lecture_schedule.lecture_id   <br>
Ref: lecture_schedule.lecture_id < enrollment.lecture_id   <br>

<br>
</details>
   
### 개발 과정
#### 2024.06.23 
- spring 세팅
- 요구사항 분석
- ERD 작성 및 구축
- LectureController 구축 및 Controller API 단위 테스트 -> ${\textsf{\color{magenta}	그런데 controller 먼저 하자니 TC 변경이 너무 많다.. 내부로직인 Domain부터 뻗어가는게 맞을까? 이게 bottom up 인가?}}$ <br>  

#### 2024.06.24 
- Controller 날짜 관련 검증 어노테이션 추가 <br>
- Service 테스트 케이스 작성 및 구축 <br>
- 메세지 관리용 enum 클래스 생성  <br> 

#### 2024.06.25 (1차 제출일 , 목표 : ERD구축(완료) , 2가지API구축(진행예정) , 아키텍처처리(진행중)
- Repository 연결 테스트 및 복합키일때 값 적재 방식 테스트 <br>
- 서비스 TDD 기반 구현 <br>

#### 2024.06.26
- 서비스 TDD 기반 구현 <br>
- 동시성 테스트(비관락 사용) <br>

#### 2024.06.27 
- 멘토링 예정 <br>
- 이후 리팩토링 진행 <br>

#### 2024.06.28 (2차 제출일) 
--- 

### 궁금증 적어놓기 

- 탑다운과 바텀업의 차이나 장단점은 뭘까? 

---

### 알게된 사실 

<details>
   <summary> <b>1. DTO 와 Domain객체의 차이와 나누는 이유</b> </summary>
DTO는 데이터 전달 및 표현에 집중, 비즈니스 로직이 없음 <br> 
Domain은 도메인 비즈니스 로직 존재 , 데이터 상태 관리 가능,  DB 엔터티와 매핑 가능 <br><br>

(1) 데이터 은닉 <br>
Domain은 모든 비즈니스 데이터 포함, 민감 정보 포함 <br> 
DTO는 선택적으로 필요 데이터만 노출 및 전송 가능 <br> 

(2) 유지보수 유연성 <br>
DTO를 이용해 인터페이스를 안정적으로 유지 <br><br> 

(3) 유효성 검증 측면 <br> 
DTO 와 Domain 둘다 가능  <br>
보통 DTO는 기본적인 검증 <br>
Domain은 비즈니스 로직 및 자신의 데이터 변경등에 대한 검증 필요시 유효성 처리 <br>
service는 트랜잭션 관리나 도메인 객체간의 조합에 집중할 수 있음 <br> <br>

</details>

<details>
   
   <summary> <b>2. 전역 예외 처리기를 이용하면 404 NotFound 와 같은 오류를 커스텀화 할 수 있음</b> </summary>
   
(1) ControllerAdvice로 특정 예외를 캐치 및 커스터마이징 가능 <br> 

```java
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(NoHandlerFoundException.class) //404 예외 커스터마이징  
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        return new ResponseEntity<>("올바르지 않은 경로입니다", HttpStatus.NOT_FOUND);
    }
}
```

</details>

<details>
   <summary> <b>3. 잘못된 필드명은 @RequestBody에서 그냥 무시하고 매핑할 수 있음</b> </summary>

   
(1) ControllerAdvice로 특정 예외를 캐치 및 커스터마이징 가능 <br> 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/74ba7919-cd52-44f1-8626-48449dab54f9)

예) userId가 필드명인데 u 로 보내고 정상통과가 되었음 <br> 
해결방안 : ObjectMapper에 FAIL_ON_UNKNOWN_PROPERTIES 을 설정해 알수없는 필드에 예외를 발생시키도록 세팅 및 테스트를 해보겠다. <br> 
결과 : 정상 처리 되었다. 나중에 Controller 추가로 구현하면서 해당 옵션을 Controller단으로 올려야 겠다 <br> 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/a67a17c4-dd10-4cd6-b6a5-c83436240780)



</details>


<details>
   <summary> <b>4. @NotNull , @NotEmpty , @NotBlank </b> </summary>

@NotBlank : null 안됨 , 비어있으면 안됨 , 공백문자만 있는거 안됨
@NotEmpty : null 안됨 , 비어있으면 안됨
@NotNull  : null 안됨

</details>



<details>
   <summary> <b>5. 비관적 락과 낙관적 락 , 언제 뭘 쓰는게 좋을까?  </b> </summary>

[ 참고 출처 : https://velog.io/@bagt/Database-%EB%82%99%EA%B4%80%EC%A0%81-%EB%9D%BD-%EB%B9%84%EA%B4%80%EC%A0%81-%EB%9D%BD ] 

#### 비관적 락 
Repeatable Read : update 방지 , insert 방지 안됨 , 공유락 이라고도 함
Serializable Read : update 방지 , insert 방지 , 배타락 이라고도 함

#### 낙관적 락 
- 자원에 락을 걸지않고 "동시성 문제가 발생하면 그때 처리한다" 
- 별도의 컬럼을 추가 (version , hashcode , timestamp 등) 해서 충돌을 막는다.
- UPDATE에 실패한다고 해도 예외가 아니라 0row를 리턴하므로 앱단에서 롤백등을 처리할 수 있다.
- @Version을 이용해 낙관적 락을 구현하고, 낙관적 락 발생시 ObjectOptimisticLockingFailureException 예외를 발생시킨다.

<br>
낙관적 락 예시 
(1) 처리할 도메인에 @Version 필드 추가 ( 동시성 제어 목적, 현재 데이터 버전과 트랜잭션 시작 시점을 비교해 일치하면 업데이트 , 일치하지 않으면 충돌 알림) 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/fc32e9cb-e85e-4edb-bcf1-514f6ba17fee)

(2) Service 계층에서 save 메서드 호출시 OptimisticLockException (충돌시 발생하는 낙관적 락 예외) 을 처리하도록 함 

(3) 근데 insert 목적이라면 낙관적 락의 의미가 없지 않나 .. 다수 인스턴스 환경이라면 key error 일거고
요지는 수강 신청 대상이 30명일때를 조회하는 시점을 , 비관적으로 막을 건지 낙관적으로 막을 건지를 보는건가? 

#### 성능은 누가 더 좋을까? 

if) 충돌이 별로 없다면
낙관적 락이 더 좋다 -> 따로 트랜잭션 필요 없기 때문에 
비관적 락은 데이터 자체에 락을 걸어 동시성 저하 및 데드락 발생 가능성 있음 

if) 충돌이 잦다면? 
비관적 락이 더 유리하다 -> 트랜잭션 롤백하면 끝
낙관적 락은 수동 롤백 + update 처리도 해줘야 한다 
<br> 

이 구현과제에서는 뭐가 더 유리할까?

처음에는 INSERT 를 고려하여 낙관적 락으로 진행을 했다. (왜냐면 INSERT는 충돌이 거의 발생하지 않을 것이기 때문이다)
문제는 조회시점에 정원 30명 초과를 제대로 처리하지 못했기에 인원수를 컬럼으로 추가하고 UPDATE하는 방향으로 진행했다.
이 경우 UPDATE가 매우 활발히 발생하는 충돌이 높은 환경이므로 비관적 락을 사용해 처리했다. 

<br> 

</details>


<details>
   <summary> <b>6. 테스트 환경에서는 @Transactional 을 쓰면 메서드 완료 후 자동 롤백된다.  </summary>
</details>

<details>
   <summary> <b>7. jpql 에서 테이블이름은 엔터티명칭을 써야한다. 컬럼도.. </summary>
</details>

---

### 이슈 정리 

#### 1. Lombok이 안먹혔던 이슈

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/2e72d07d-7ada-4268-b7b7-889bd007690c)

#### 2. 비관적 락을 해봤지만 .. 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/8b6c8bba-02ca-46d0-a691-e01c5f19fa39)

findByLectureIdAndLectureDyWithPessimisticLock 호출 시점에 비관락을 걸었다.  <br>
목적은 해당 테이블을 읽을 때 Lock을 걸어 다른 대상이 처리 못하게 하는 것이었는데  <br>
비관 락은 특정 행의 update 나 select 에 영향을 주지만 , insert에는 주지 않아 계속 추가적인 값이 들어갔다.  <br>
<br>
어떻게 하면 DB에 30명까지만 들어가게 할 수 있을까? 접근 방식을 조금 바꿨다.<br>
PESSIMISTIC_WRITE 특성이 테이블 내의 행을 비관적으로 잠근다고 한다. (테이블단위 아니라는 소리)<br>
그럼 수강일정 테이블에 신청현황 컬럼을 추가하고 이 값을 UPDATE 할때 비관락을 한다면 어떨까? (여러 행에 대해 lock이 발생하지만 테이블 단위는 아니므로 접근 가능) <br>
<br>
즉, 신청이 들어올 경우 수강일정컬럼의 수강현황을 비관적 lock 후 읽어 30이면 더이상 들어갈 수 없게 한다. <br>
만약 30을 넘지 않으면 +1을 update한다. 이후 save() 을 진행한다.<br>
<br>
비관적락은 SELECT FOR UPDATE , 즉 UPDATE를 예상하고 해당 행에 락을 거는 것이다. <br>
따라서 SELECT 부분에 LOCK처리를 하고 이후에 UPDATE를 하면 된다. (난 JPA를 처음 공부하다보니 UPDATE에 LOCK을 설정하는 등 삽질을 좀 했다..) <br>
<br>


### TDD를 하다보면서 느낀 점

<br>

1. 안정감 : 최소 이거는 실행되나? 를 기점으로 로직에 살을 붙이다 보니 잠재 오류를 최소화 할 수 있다.<br>
<br>
2. 자신감 : 코드를 작성하면서 불안감이 많이 해소된다.<br>
<br>
3. 나중에는 모든 테스트가 통과해야만 빌드를 하므로 안전 장치로 작동을 할 수 있다.<br>
<br>
4. 하지만 아직은 큰 로직이 변경 되거나 할때 TC를 제대로 활용하지 못하는 거 같다.<br>
<br>
5. 테스트 자동화의 개념을 공부해봐야 겠다.<br>
<br>
