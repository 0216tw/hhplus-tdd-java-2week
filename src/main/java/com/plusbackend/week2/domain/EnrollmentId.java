package com.plusbackend.week2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EnrollmentId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "lecture_id")
    private Long lectureId;

    @Column(name = "lecture_dy")
    private String lectureDy;

}