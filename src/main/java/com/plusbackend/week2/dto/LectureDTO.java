package com.plusbackend.week2.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class LectureDTO {
    private long lectureId ;
    private String lectureName;
    private long maxCapacity;
    private String lectureInfo ;

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public long getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getLectureInfo() {
        return lectureInfo;
    }

    public void setLectureInfo(String lectureInfo) {
        this.lectureInfo = lectureInfo;
    }
}
