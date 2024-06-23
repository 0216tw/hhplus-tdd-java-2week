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
  
1. 사용자는 특정 userId 로 식별된다.  <br> 
2. 강의에 대한 정원은 30명이다.  <br> 
3. 정원이 초과된 강의에 대해서는 수강신청을 실패해야 한다. <br>
4. 사용자가 신청한 특강정보는 히스토리로 저장된다.  <br>
5. 특정 강의를 특정 일자에 신청 성공한 경우, 동일한 강의,일자에 추가 신청할 수 없다.   <br>
6. 특강 신청 완료 여부를 조회하며 해당 정보가 있으면 true , 없으면 false (혹은 예외 알림) 등으로 처리한다.  <br>
</details>

<details> 
<summary>
   <b>추가 도출한 요구사항</b>
</summary>
1. 강의에 대한 정원을 30명이 아니라 각 강의마다 동적으로 처리할 수 있게 하면 확장성을 구할 수 있지 않을까? DB에 해당 컬럼을 추가하자 <br>
2. <br>
</details>

<br>

<h2>ERD </h2>
<details> 
<summary>
   <b>설계하면서 고민했던 것</b>
  1. 사용자가 수강신청을 취소한다면 ? <br>
  수강신청목록에서는 삭제하고, 이력에는 상태값을 이용해서 저장을 하자 <br>
  예를 들어 신청하면 수강신청목록에도 insert , 이력에도 상태값=신청 으로 insert <br>
  만약 취소를 한다면 수강신청목록에는 delete , 이력에는 상태값=취소로 insert <br>


</summary>


<br><br>

### 개발 과정
2024.06.23 요구사항 분석, ERD 작성 및 구축



<br><br> 


--- 
궁금증 적어놓기 

다수의 인스턴스라고 한다면 각 사용자의 end-point를 의미하는건가? 그럼 동시성 이슈란 여러 사용자가 하나의 DB에 달려들때의 이슈를 말하는 걸까? <br>
사용자가 특강 신청을 실패한 경우도 이력을 저장해야 하는가? 보통 현업에서는 실패 정보도 저장해야할까? 아니면 굳이 할 필요가 없을까?  <br> 


