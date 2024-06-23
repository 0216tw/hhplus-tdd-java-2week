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
2024.06.23 요구사항 분석, ERD 작성 및 구축



<br><br> 


--- 
궁금증 적어놓기 

다수의 인스턴스라고 한다면 각 사용자의 end-point를 의미하는건가? 그럼 동시성 이슈란 여러 사용자가 하나의 DB에 달려들때의 이슈를 말하는 걸까? <br>



