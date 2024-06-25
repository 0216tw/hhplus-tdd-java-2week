package com.plusbackend.week2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Collate;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LectureScheduleId implements Serializable {

    @Column(name = "lecture_id")
    private long lectureId;

    @Column(name = "lecture_dy")
    private String lectureDy;

}
