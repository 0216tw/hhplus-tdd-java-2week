package com.plusbackend.week2.integration.repository;



import com.plusbackend.week2.domain.*;
import com.plusbackend.week2.repository.EnrollmentRepository;
import com.plusbackend.week2.repository.LectureRepository;
import com.plusbackend.week2.repository.LectureScheduleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


/*
 * 테스트 정보
 * Lecture 테이블에 정상 insert 되는지 확인 + 수강가능 목록 조회
 */
public class LectureScheduleRepositoryTest extends RepositoryTestBase{


    @Autowired
    private LectureScheduleRepository lectureScheduleRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;


    @Test
    public void 수강신청_완료대상_제외하고_수강목록_출력하기_성공() throws InterruptedException {
        //given
        Lecture lecture1 = new Lecture(1L , "플러스백엔드5기특강"  , 30L , "이 강의는 말이죠..~~");
        Lecture lecture2 = new Lecture(2L , "플러스프론트엔트2기특강" , 30L , "이 강의는 프론트란 말이죠~~");

        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        List<Lecture> lectures = lectureRepository.findAll();

        long lectureId1 = lectures.get(0).getLectureId();
        long lectureId2 = lectures.get(1).getLectureId();

        LectureScheduleId lectureScheduleId1 = new LectureScheduleId(lectureId1 , "20240624");
        LectureScheduleId lectureScheduleId2 = new LectureScheduleId(lectureId1 , "20240625");
        LectureScheduleId lectureScheduleId3 = new LectureScheduleId(lectureId2 , "20240626");
        LectureScheduleId lectureScheduleId4 = new LectureScheduleId(lectureId2 , "20240627");

        Date startDate = Date.from(LocalDateTime.of(2024, Month.JUNE , 25 , 9 , 00).atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDateTime.of(2024, Month.JUNE , 25 , 11 , 00).atZone(ZoneId.systemDefault()).toInstant());

        LectureSchedule lectureSchedule1 = new LectureSchedule(lectureScheduleId1  , lectures.get(0) , startDate , endDate );
        LectureSchedule lectureSchedule2 = new LectureSchedule(lectureScheduleId2  , lectures.get(0), startDate , endDate );
        LectureSchedule lectureSchedule3 = new LectureSchedule(lectureScheduleId3   , lectures.get(1), startDate , endDate );
        LectureSchedule lectureSchedule4 = new LectureSchedule(lectureScheduleId4  , lectures.get(1), startDate , endDate );

        EnrollmentId enrollmentId1 = new EnrollmentId(1L , lectureScheduleId1.getLectureId() , lectureScheduleId1.getLectureDy());
        EnrollmentId enrollmentId2 = new EnrollmentId(1L , lectureScheduleId3.getLectureId() , lectureScheduleId3.getLectureDy());
        Enrollment enrollment1 = new Enrollment(enrollmentId1);
        Enrollment enrollment2 = new Enrollment(enrollmentId2);


        //when

        lectureScheduleRepository.save(lectureSchedule1);
        lectureScheduleRepository.save(lectureSchedule2);
        lectureScheduleRepository.save(lectureSchedule3);
        lectureScheduleRepository.save(lectureSchedule4);

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);

        List<LectureSchedule> lectureSchedules = lectureScheduleRepository.findAll();
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        List<LectureSchedule> getAvailableLectures = lectureScheduleRepository.getAvailableLectures(1L);

        //then
        Assertions.assertThat(lectureSchedules).isNotNull();
        Assertions.assertThat(lectureSchedules.size()).isEqualTo(4);
        Assertions.assertThat(lectureSchedules).extracting(LectureSchedule::getId)
                .contains(lectureScheduleId1 ,lectureScheduleId2 , lectureScheduleId3, lectureScheduleId4);

        Assertions.assertThat(enrollments).isNotNull();
        Assertions.assertThat(enrollments.size()).isEqualTo(2);
        Assertions.assertThat(enrollments).extracting(Enrollment::getId).contains(enrollmentId1 , enrollmentId2);

        Assertions.assertThat(getAvailableLectures).isNotNull();
        Assertions.assertThat(getAvailableLectures.size()).isEqualTo(2);
        Assertions.assertThat(getAvailableLectures).extracting(LectureSchedule::getId)
                .contains(lectureScheduleId2 , lectureScheduleId4);

    }


}
