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
1. 사용자는 userId 로 식별함 <br> 
2. 강의 정원은 30명<br> 
3. 정원 초과시 수강신청 실패 <br>
4. 신청 정보는 히스토리 저장 <br>
5. 특정날짜/특정강의 신청 후 동일 건 신청 불가 <br>
6. 특강 신청 성공한 내역 리스트 응답 <br>
7. 신청 가능한 특강 목록 리스트 응답 <br> 
</details>

<details> 
<summary>
   <b>추가 도출한 사항</b>
</summary>
<br> 
1. 컬럼 추가시 강의 정원 n명으로 구성 가능 <br> 
2. ... <br>
</details>

<br>

<h2>ERD (데이터 모델링 & DBML) -> 필요시 수정해야하는데 dbdiagram 터졌나 ??  </h2> <br>

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/620fa04b-b082-4b22-8f72-fb055afa6912)

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
   
<details> 
<summary>
   <b>설계하면서 고민했던 것</b>
</summary>
<br>
  <b>1. 수강 신청하고 취소하면? </b> <br>
  수강신청목록(enrollment) 테이블에서 DELETE , 수강신청목록이력(enrollment_history) 테이블에 INSERT<br>
  예) 수강 신청시 enrollment -> insert , enrollment_history -> insert (상태값 = 신청) <br>
  예) 수강 취소시 enrollment -> delete , enrollment_history -> insert (상태값 = 취소) <br>
  두 테이블 사이 외래키 조건은 없음 (원본 삭제해도 이력 남기기 목적) <br><br>
 
 <b>2.동일한 특강, 다른 날에도 가능</b>  <br>
 다른 날짜에 같은 특강을 또 할 수 있다면 특강ID + 날짜를 PK로 해야 중복발생 안함 -> <span style="color:red">고민중.. 성능을 고려하면 식별관계가 맞지 않나? 비식별로 해도 괜찮을까? 및 이유가 있나? </span> <br>  
 특강일정(lecture_schedule) 테이블 -> lecture_id , lecture_dy <br>
 사용자(user)테이블 -> user_id <br>
 위 세게 조합해 수강신청(enrollment) 테이블 키 조합 처리 <br> 
 시간까지는 고려하지 않음 <br><br>
 
<b>4. 특강 정원을 FIX 해야할까? </b>   <br>
      컬럼 추가를 해서 동적으로 만들어보자 <br> <br>

<b>5. 사용자는 회원/비회원 모두 신청 가능?</b>   <br>  
가능하다면 신청시 name , phone 을 필수로 받아서 phone에 unique 조건 주고 대상을 식별하자 <br><br>
</details> 

<br><br>

### 개발 과정
#### 2024.06.23 
- spring 세팅
- 요구사항 분석
- ERD 작성 및 구축
- LectureController 구축 및 Controller API 단위 테스트 -> ${\textsf{\color{magenta}	그런데 controller 먼저 하자니 TC 변경이 너무 많다.. 내부로직인 Domain부터 뻗어가는게 맞을까? 이게 bottom up 인가?}}$ <br>  

#### 2024.06.24 
- Controller 날짜 관련 검증 어노테이션 추가
- Service 테스트 케이스 작성 및 구축
- 메세지 관리용 enum 클래스 생성  
- 

<br><br> 


--- 

### 궁금증 적어놓기 

- 탑다운과 바텀업의 차이나 장단점은 뭘까? 


---

### 알게된 사실 

#### 1. DTO 와 Domain객체의 차이와 나누는 이유 
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

#### 2. 전역 예외 처리기를 이용하면 404 NotFound 와 같은 오류를 커스텀화 할 수 있음

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

#### 3. 잘못된 필드명은 @RequestBody에서 그냥 무시하고 매핑할 수 있음

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/74ba7919-cd52-44f1-8626-48449dab54f9)

예) userId가 필드명인데 u 로 보내고 정상통과가 되었음 <br> 
해결방안 : ObjectMapper에 FAIL_ON_UNKNOWN_PROPERTIES 을 설정해 알수없는 필드에 예외를 발생시키도록 세팅 및 테스트를 해보겠다. <br> 
결과 : 정상 처리 되었다. 나중에 Controller 추가로 구현하면서 해당 옵션을 Controller단으로 올려야 겠다 <br> 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/a67a17c4-dd10-4cd6-b6a5-c83436240780)


#### 4. @NotNull , @NotEmpty , @NotBlank 

@NotBlank : null 안됨 , 비어있으면 안됨 , 공백문자만 있는거 안됨
@NotEmpty : null 안됨 , 비어있으면 안됨
@NotNull  : null 안됨

---

#### 5. 비관적 락과 낙관적 락 , 언제 뭘 쓰는게 좋을까? 
[ 참고 출처 : https://velog.io/@bagt/Database-%EB%82%99%EA%B4%80%EC%A0%81-%EB%9D%BD-%EB%B9%84%EA%B4%80%EC%A0%81-%EB%9D%BD ] 

#### 비관적 락 


#### 낙관적 락 
- 자원에 락을 걸지않고 "동시성 문제가 발생하면 그때 처리한다" 
- 별도의 컬럼을 추가 (version , hashcode , timestamp 등) 해서 충돌을 막는다.
- UPDATE에 실패한다고 해도 예외가 아니라 0row를 리턴하므로 앱단에서 롤백등을 처리할 수 있다.
- @Version을 이용해 낙관적 락을 구현하고, 낙관적 락 발생시 ObjectOptimisticLockingFailureException 예외를 발생시킨다.
- 



---

### 이슈 정리 

#### 1. Lombok이 안먹혔는데 아래와 같이 처리해서 해결

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/2e72d07d-7ada-4268-b7b7-889bd007690c)

#### 2. 
mockMvc를 이용해서 부적절한 URL 등을 테스트하려고 할때 404 not found 예외가 어드바이스에 잡혀야하는데  <br>
perform 은 오직 Exception을 처리한다.. 따로 404 나 400 을 리턴하는 방법은?  <br>
혹은 단위테스트 레벨에서는 이 방법은 불가능한 것인가? @WebMvcTest 쓰면 되지 않나?  <br>

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/5e306577-27af-498d-a752-8a935a1baba4)

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/b110bc68-8576-4d24-998e-af170764f73c)

문제해결! <br> 
먼저 Advice에 Exception.class 예외 처리를 제거했다. (아마 예외가 구체적으로 바뀌기 전에 Exception으로 그냥 처리해버린게 아닐까..이건 더 알아봐야한다) <br> 

위 Exception을 제거했더니 테스트 케이스에 대해서 404 , 405 등이 제대로 떴다.  <br> 

그런데 404같은 경우에 NoHandlerFoundException 가 아니라 NoResourceFoundException 으로 해야 404가 잡혔다. <br> 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/7faa677a-fe3a-4399-9d6b-c4a96bd01c13)

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/3c92025a-e341-499f-98c8-6fbe4d68583a)

근데 이렇게 예외를 상세하게 하는것이 좋나? 아니면 500 에러로 추상화하는게 좋은가? 

