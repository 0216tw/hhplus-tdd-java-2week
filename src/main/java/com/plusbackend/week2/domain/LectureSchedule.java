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

    @ManyToOne
    @MapsId("lectureId") // This maps the lectureId attribute of the composite key
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private Date startDate;
    private Date endDate ;
}
