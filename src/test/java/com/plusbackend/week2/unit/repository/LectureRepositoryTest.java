package com.plusbackend.week2.unit.repository;



import com.plusbackend.week2.domain.Lecture;
import com.plusbackend.week2.repository.LectureRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.util.List;

/*
 * 강의 DB 테이블 생성 및 데이터 적재 테스트
 */
public class LectureRepositoryTest extends RepositoryTestBase{


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

        //when
        List<Lecture> lectures = lectureRepository.findAll();

        Assertions.assertThat(lectures).isNotNull();
        Assertions.assertThat(lectures.size()).isEqualTo(2);
        Assertions.assertThat(lectures).extracting(Lecture::getLectureName).contains("플러스백엔드5기특강", "플러스프론트엔트2기특강");

    }


}
