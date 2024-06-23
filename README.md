# hhplus-tdd-java-2week (수강 신청 관리 구현)

### 구현 과제 
- 특강 신청 API 
- 특강 신청 여부 확인 API 
- 특강 가능 목록 조회 API 
- 고려할 것 : 클린코드 , 동시성제어 , TDD , 

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
1. 사용자는 특정 userId 로 식별된다.  <br> 
2. 강의에 대한 정원은 30명이다.  <br> 
3. 정원이 초과된 강의에 대해서는 수강신청을 실패해야 한다. <br>
4. 사용자가 신청한 특강정보는 히스토리로 저장된다.  <br>
5. 특정 강의를 특정 일자에 신청 성공한 경우, 동일한 강의,일자에 추가 신청할 수 없다.   <br>
6. 특강 신청 완료 여부를 조회하며 해당 정보가 있으면 true , 없으면 false (혹은 예외 알림) 등으로 처리한다.  <br>
</details>

<details> 
<summary>
   <b>추가 도출한 사항</b>
</summary>
   <br> 
1. 강의에 대한 정원을 30명이 아니라 각 강의마다 동적으로 처리할 수 있게 하면 확장성을 구할 수 있지 않을까? DB에 해당 컬럼을 추가하자 <br>
2. <br>
</details>

<br>

<h2>ERD (데이터 모델링 & DBML) </h2> <br>

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/620fa04b-b082-4b22-8f72-fb055afa6912)

<a href="https://dbdiagram.io/d/6677a8115a764b3c722aa3d5">링크</a> 

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
  <b>1. 사용자가 수강신청을 취소한다면 ? </b><br>
  수강신청목록(enrollment) 테이블에서는 대상을 delete 하자. 그리고 수강신청목록이력(enrollment_history) 에는 상태값을 추가로 만들어 저장하자<br>
  예) 수강 신청시 enrollment -> insert , enrollment_history -> insert (상태값 = 신청) <br>
  예) 수강 취소시 enrollment -> delete , enrollment_history -> insert (상태값 = 취소) <br>
  enrollment 와 enrollment_history 사이에 외래키 제약은 걸지 않는다. (이유 : 원본이 삭제되어도 이력은 남기자는 취지) <br>

<br>
 <b>2. 동일한 특강을 다른 날에도 할 수 있다.</b>  <br>
 같은 특강도 다른 날짜에 또 강의할 수 있다. <br>
 그럼 특강id + 날짜로 primary key 를 설정해야 중복이 발생하지 않는다. <br>
 특강(lecture) 테이블에는 특강 고유정보를 저장하고, 엔터티를 추가해 특강일정(lecture_schedule) 으로 날짜도 함께 저장한다.<br>
 이제 특강신청시 (user_id , enrollment_id , enrollment_dy 조합으로 유일한 식별이 가능하다) <br>
 
<br> 
 <b>3. 동일한 특강을 하루에 시간단위로 여러번 한다면? </b>  <br>
 일단 여기서 시간은 고려하지 말고, 특강은 "하루에 한번만" 진행하는 것으로 범위를 좁힌다. <br>
 
<br>
<b>4. 특강 정원은 특강별로? 아니면 특강 날짜마다 다르게? </b>   <br>
      동일한 특강은 날짜별로 다르게 진행될 수 있다. <br>
      날짜마다 정원을 유동적으로 관리할 수는 있겠지만, 여기서도 범위를 좁혀서 특강 정원은 날짜가 달라고 동일하게 fix하자 <br>
<br>
<b>5. 사용자는 회원/비회원 모두 신청 가능한가?</b>   <br>  
      회원/비회원 모두 가능하다면 user_id를 통한 사용자 식별이 의미없다고 생각한다. (왜냐면 비회원은 user_id 가 없을거라서) <br>
      그래서 신청할때 name과 phone을 필수값으로 입력받고 phone에 unique 조건을 주어서 신청한 대상을 유일 식별할 수 있게 한다.  <br>
 <br>

</details> 

<br><br>

### 개발 과정
2024.06.23 spring 세팅 요구사항 분석, ERD 작성 및 구축 , LectureController 구축 , Controller API 단위 테스트

2024.06.24 할일 
일단 아래 소스 분석 및 공부하고 날짜 검증을 위한 별도의 어노테이션 + 클래스 생성하자 
```java
package com.plusbackend.week2.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    String message() default "유효하지 않은 날짜 형식입니다. yyyyMMdd 형식이어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

```java
package com.plusbackend.week2.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private static final String DATE_FORMAT = "yyyyMMdd";

    @Override
    public void initialize(ValidDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.length() != 8 || !value.matches("\\d{8}")) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);

        try {
            sdf.parse(value);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}

 @NotBlank(message = "LectureDy는 필수 입력값입니다.")
    @ValidDate // 커스텀 날짜 검증 어노테이션 적용
    private String lectureDy; // 강의 일자 (날짜로 검증 필요)

```



<br><br> 


--- 
궁금증 적어놓기 

다수의 인스턴스라고 한다면 각 사용자의 end-point를 의미하는건가? 그럼 동시성 이슈란 여러 사용자가 하나의 DB에 달려들때의 이슈를 말하는 걸까? <br>

입력값 검증은 어디서 어떻게 해야할까? 
DTO와 JPA를 혼용할까, 따로 쓸까? 업계에서는 DTO JPA 혼용이 많다던데 



---
알게된 사실 

### 1. DTO 와 Domain객체의 차이와 나누는 이유 
DTO는 주로 계층 간 데이터 전송에 사용된다. 비즈니스 로직이 없다. <br>
Domain은 도메인 로직을 처리하고 DB 엔터티와 매핑될 수 있다. 비즈니스 로직이 존재한다. <br>

<br>
(1) 책임 분리 <br>
Domain은 핵심 비즈니스 로직 포함 및 DB 엔터티로 쓰일 수 있다. 즉 데이터의 상태를 관리할 수 있다. <br>
반면 DTO은 데이터 전송(주로 service - presentation 사이) , 데이터 전달 및 표현에 집중한다. <br>
<br>
(2) 데이터 은닉 <br>
Domain은 모든 비즈니스 데이터를 포함할 수 있기에 민감 정보도 포함될 수 있다. <br>
DTO는 선택적으로 필요한 데이터만 노출할 수 있어 은닉성이 증가한다. <br>
<br>
(3) 유지보수 유연성 <br>
Domain은 애플리케이션 내부 구현과 밀접하다. 이를 직접 노출시키면 유지보수시 많은 수정이 필요할 수 있다. <br>
DTO를 이용해서 인터페이스를 안정적으로 유지할 수 있다.<br>
<br>
(4) 전송의 효율성 <br>
DTO는 필요한 데이터만 저장하면 된다. <br>

<br> 

그럼 유효성 검증은 어디서 하지? <br> 
DTO 와 Domain 둘다 가능한데 보통 DTO는 기본적인 검증을 하고, Domain은 비즈니스 로직에 따른 검증이 필요할 때 진행<br>
<br>
그리고 Domain에 핵심 로직이 들어가면..Service에서 해도 되지 않나?? <br>
=> Domain에 핵심 비즈니스 로직을 넣어서 상태를 관리하고 객체지향성을 강화한다.<br>
Service는 트랜잭션 관리나 도메인 객체간의 조합을 담당한다 <br>

잘못된 HTTP 메서드로 요청이 온 경우를 테스트 할 수 있나? -> 전체 웹 애플리케이션 컨텍스트를 시뮬레이션하므로 검증 가능하다고 함

### 2. 전역 예외 처리기를 이용하면 404 NotFound 와 같은 오류를 커스텀화 할 수 있다.

(1) ControllerAdvice를 이용해 특정 예외를 캐치해 메세지를 설정한다. (컨트롤러 전반적으로 처리가 가능하다) <br> 
```java
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(NoHandlerFoundException.class) //404 예외를 캐치한다. 
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        return new ResponseEntity<>("올바르지 않은 경로입니다", HttpStatus.NOT_FOUND); //메세지와 오류를 새롭게 정해 반환 
    }
}
```
(2) Spring Boot의 application.properties에 아래와 같이 설정을 추가한다 <br> 

```java
spring.mvc.throw-exception-if-no-handler-found=true  #Spring Mvc 가 적절한 핸들러를 찾기 못한경우 예외를 던지도록 처리 
``` 

### 3. 잘못된 필드명은 @RequestBody에서 그냥 무시하고 매핑할 수도 있다. 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/74ba7919-cd52-44f1-8626-48449dab54f9)


예) userId가 필드명인데 u 로 보내고 정상통과가 되었음 <br> 
그래서 DTO에 필수값에 대한 @NotNull , @NotEmpty 등 validation을 추가하고 예외 전처리기에 MethodArgumentNotValidException 을 추가해보면 음 안된다! <br> 
다른방법을 써보자 <br> 
이번에는 ObjectMapper에 FAIL_ON_UNKNOWN_PROPERTIES 을 설정해 알수없는 필드에 예외를 발생시키도록 세팅 및 테스트를 해보겠다. <br> 
정상 처리 되었다. 나중에 Controller 추가로 구현하면서 해당 옵션을 Controller단으로 올려야 겠다 <br> 

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/a67a17c4-dd10-4cd6-b6a5-c83436240780)





### 4. @NotNull , @NotEmpty , @NotBlank 

@NotBlank : null 안됨 , 비어있으면 안됨 , 공백문자만 있는거 안됨
@NotEmpty : null 안됨 , 비어있으면 안됨
@NotNull  : null 안됨

---

이슈 정리 

### 1. Lombok이 안먹혔는데 아래와 같이 처리해서 해결

![image](https://github.com/0216tw/hhplus-tdd-java-2week/assets/140934688/2e72d07d-7ada-4268-b7b7-889bd007690c)

### 2. 
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

