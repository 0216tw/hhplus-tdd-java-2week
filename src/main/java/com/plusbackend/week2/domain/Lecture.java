package com.plusbackend.week2.domain;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lectureId;

    private String lectureName;
    private long maxCapacity;
    private String lectureInfo;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LectureSchedule> lectureSchedules;

    public Lecture(long lectureId, String lectureName, long maxCapacity, String lectureInfo) {
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.maxCapacity = maxCapacity;
        this.lectureInfo = lectureInfo;
    }
}
