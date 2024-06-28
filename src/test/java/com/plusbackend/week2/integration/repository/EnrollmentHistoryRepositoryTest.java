package com.plusbackend.week2.integration.repository;



import com.plusbackend.week2.domain.EnrollmentHistory;
import com.plusbackend.week2.domain.Lecture;
import com.plusbackend.week2.repository.EnrollmentHistoryRepository;
import com.plusbackend.week2.repository.LectureRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/*
 * 테스트 정보
 * Lecture 테이블에 정상 insert 되는지 확인
 */
public class EnrollmentHistoryRepositoryTest extends RepositoryTestBase{

    @Autowired
    private EnrollmentHistoryRepository enrollmentHistoryRepository;

    @Test
    @Rollback
    public void insert() {

        EnrollmentHistory enrollmentHistory1 = new EnrollmentHistory(1L , 1L , "20240626" , "reg");
        EnrollmentHistory enrollmentHistory2 = new EnrollmentHistory(1L , 1L , "20240626" , "cnl");
        EnrollmentHistory enrollmentHistory3 = new EnrollmentHistory(1L , 1L , "20240627" , "reg");


        enrollmentHistoryRepository.save(enrollmentHistory1);
        enrollmentHistoryRepository.save(enrollmentHistory2);
        enrollmentHistoryRepository.save(enrollmentHistory3);

        List<EnrollmentHistory> enrollmentHistoryRepositories = enrollmentHistoryRepository.findAll();

        Assertions.assertThat(enrollmentHistoryRepositories).isNotNull();
        Assertions.assertThat(enrollmentHistoryRepositories.size()).isEqualTo(3);
        Assertions.assertThat(enrollmentHistoryRepositories).extracting(EnrollmentHistory::getState).contains("reg" , "cnl");


    }


}
