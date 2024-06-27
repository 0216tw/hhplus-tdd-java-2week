package com.plusbackend.week2.enums;

public enum ResponseMessage {
    ENROLLMENT_SUCCESS("수강신청이 완료되었습니다.") ,
    NO_SUCH_LECTURE_SCHEDULE("존재하지 않은 강의일정입니다.") ,
    BAD_REQUEST("잘못된 요청입니다. 요청 데이터를 확인해주세요.") ,
    NOT_FOUND("올바르지 않은 경로입니다."),
    METHOD_NOT_ALLOWED("올바르지 않은 HTTP 메서드입니다.") ,
    ALREADY_ENROLLED("이미 수강신청 완료된 대상입니다.") ,
    ENROLLMENT_FAILURE("수강신청에 실패하였습니다.") ,
    MAXIMUM_CAPACITY_EXCEEDED("수강 정원을 초과하였습니다.") ,
    SEARCH_OK("조회완료되었습니다.") ,
    CONCURRENCY_FAILURE("동시성 오류 발생") ;

    private final String message ;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
