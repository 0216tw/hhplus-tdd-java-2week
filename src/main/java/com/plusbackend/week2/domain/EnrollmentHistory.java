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
    private long userId;
    private long lectureId;
    private String lectureDy;


}
