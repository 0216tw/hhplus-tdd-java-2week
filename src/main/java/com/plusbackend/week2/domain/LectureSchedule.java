package com.plusbackend.week2.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lecture_schedule")
public class LectureSchedule {

    @EmbeddedId
    private LectureScheduleId id;

    @ManyToOne(fetch = FetchType.LAZY) //엔터티가 실제 사용될때까지 로딩 지연으로 불필요한 데이터를 가져오지 않게 함
    @MapsId("lectureId") // This maps the lectureId attribute of the composite key
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private Date startDate;
    private Date endDate ;
    private long enrollCount ;

    public LectureSchedule(LectureScheduleId id, Lecture lecture, Date startDate, Date endDate) {
        this.id = id;
        this.lecture = lecture;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
