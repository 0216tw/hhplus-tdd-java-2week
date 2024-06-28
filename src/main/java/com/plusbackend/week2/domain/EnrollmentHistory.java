package com.plusbackend.week2.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "enrollment_history")
public class EnrollmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;
    //이력성이므로 Enrollment와 관계 설정을 하지 않는다.

    @Column(name = "user_id")
    private long userId;

    @Column(name = "lecture_id")
    private long lectureId;

    @Column(name = "lecture_dy")
    private String lectureDy;

    private String state ;

    public EnrollmentHistory(long userId, long lectureId, String lectureDy , String state) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.lectureDy = lectureDy;
        this.state = state;
    }

}
