package com.plusbackend.week2.unit.repository;



import com.plusbackend.week2.domain.Lecture;
import com.plusbackend.week2.domain.LectureSchedule;
import com.plusbackend.week2.domain.LectureScheduleId;
import com.plusbackend.week2.repository.LectureRepository;
import com.plusbackend.week2.repository.LectureScheduleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


/*
 * 강의일정 DB 테이블 생성 및 데이터 적재 테스트
 * 복합키 insert 테스트
 */
public class LectureScheduleRepositoryTest extends RepositoryTestBase{


    @Autowired
    private LectureScheduleRepository lectureScheduleRepository;

    @Autowired
    private LectureRepository lectureRepository;


    @Test
    @Commit
    public void insert() {
        //given
        Lecture lecture1 = new Lecture(1L , "플러스백엔드5기특강"  , 30L , "이 강의는 말이죠..~~");
        Lecture lecture2 = new Lecture(2L , "플러스프론트엔트2기특강" , 30L , "이 강의는 프론트란 말이죠~~");

        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        LectureScheduleId lectureScheduleId1 = new LectureScheduleId(lecture1.getLectureId() , "20240624");
        LectureScheduleId lectureScheduleId2 = new LectureScheduleId(lecture1.getLectureId() , "20240625");
        LectureScheduleId lectureScheduleId3 = new LectureScheduleId(lecture2.getLectureId() , "20240626");
        LectureScheduleId lectureScheduleId4 = new LectureScheduleId(lecture2.getLectureId() , "20240627");

        Date startDate = Date.from(LocalDateTime.of(2024, Month.JUNE , 25 , 9 , 00).atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDateTime.of(2024, Month.JUNE , 25 , 11 , 00).atZone(ZoneId.systemDefault()).toInstant());

        LectureSchedule lectureSchedule1 = new LectureSchedule(lectureScheduleId1  , lecture1 , startDate , endDate );
        LectureSchedule lectureSchedule2 = new LectureSchedule(lectureScheduleId2  , lecture1, startDate , endDate );
        LectureSchedule lectureSchedule3 = new LectureSchedule(lectureScheduleId3   , lecture2, startDate , endDate );
        LectureSchedule lectureSchedule4 = new LectureSchedule(lectureScheduleId4  ,lecture2, startDate , endDate );

        lectureScheduleRepository.save(lectureSchedule1);
        lectureScheduleRepository.save(lectureSchedule2);
        lectureScheduleRepository.save(lectureSchedule3);
        lectureScheduleRepository.save(lectureSchedule4);

        //when
        List<LectureSchedule> lectureSchedules = lectureScheduleRepository.findAll();

        Assertions.assertThat(lectureSchedules).isNotNull();
        Assertions.assertThat(lectureSchedules.size()).isEqualTo(4);
        Assertions.assertThat(lectureSchedules).extracting(LectureSchedule::getId)
                .contains(lectureScheduleId1 ,lectureScheduleId2 , lectureScheduleId3, lectureScheduleId4);

    }


}
