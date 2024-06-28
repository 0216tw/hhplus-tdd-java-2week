package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.LectureSchedule;
import com.plusbackend.week2.domain.LectureScheduleId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LectureScheduleRepositoryCustom  {

    //사용자가 신청 가능한 수강 목록을 출력
    public List<LectureSchedule> getAvailableLectures(long userId);

    //신청한 수강 대상자 수를 +1 처리
    public void updateEnrollCount(Enrollment enrollment);

    //수강 대상자 수 조회
    public long selectEnrollCount(Enrollment enrollment);
}
