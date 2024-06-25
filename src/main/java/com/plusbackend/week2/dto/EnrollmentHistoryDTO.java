package com.plusbackend.week2.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor

public class EnrollmentHistoryDTO {
    private long seq;   //이력 일련번호

    private long userId; //사용자 ID



    private long lectureId; //강의 ID
    private String lectureDy; //강의 일자 (날짜로 검증 필요) TODO
    private String enrollStmt; //신청상태 - [신청 , 취소]

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getEnrollStmt() {
        return enrollStmt;
    }

    public void setEnrollStmt(String enrollStmt) {
        this.enrollStmt = enrollStmt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public String getLectureDy() {
        return lectureDy;
    }

    public void setLectureDy(String lectureDy) {
        this.lectureDy = lectureDy;
    }
}
