package com.plusbackend.week2.unit.controller;


/*
* 수강 신청 완료 여부 조회 API 테스트
*/
public class LectureControllerApplyCompleteApiTest {

    /*
     *  실패 케이스
     *  [1] 잘못된 URL로 요청한 경우 -> 404 response  (이건 다른 경우에도 똑같으니까 봐서 리팩토링 해야겠다)
     *  [2] 잘못된 http 요청 메소드를 사용한 경우 -> 405 method not allowed (이것도 다른 경우에도 똑감음)
     *  [3] 잘못된 필드명으로 요청을 한 경우는?  -> 400 (이것도 다른 경우에 포함을 하면?)
     *  [4] 파라미터 임계값
     *      [4-1] 필수값이 없는 경우 (userId , lectureId , lectureDy) -> 400 response
     *      [4-2] 자료형이 부적합한 경우 (long , long , String) -> 400 response
     *      [4-3] 비정상적인 값이 들어오면 어떻게 되지? 예) userId에 1111111111111111111111111111111111111111111111111111111111 -> 400 response
     *      [4-4] lectureDy의 날짜 유효성이 올바르지 않을 경우 -> 400 response
     *
     *  누락한 테스트 없음 (리팩토링 필요)
     *  */


}
