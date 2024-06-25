package com.plusbackend.week2.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor

public class LectureScheduleDTO {
    private long lectureId ;
    private String lectureDy;
    private Date startDate;
    private Date endDate ;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
