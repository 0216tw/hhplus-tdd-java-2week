package com.plusbackend.week2.repository.impl;

import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.LectureSchedule;
import com.plusbackend.week2.repository.LectureScheduleRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class LectureScheduleRepositoryImpl implements LectureScheduleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LectureSchedule> getAvailableLectures(long userId) {
        String jpql = "  SELECT A " +
                "          FROM LectureSchedule A " +
                "         WHERE NOT EXISTS ( " +
                "                           SELECT 'X'  " +
                "                             FROM Enrollment e" +
                "                            WHERE e.id.lectureId = A.id.lectureId" +
                "                              AND e.id.lectureDy = A.id.lectureDy" +
                "                              AND e.id.userId = :userId )";

            return entityManager.createQuery(jpql , LectureSchedule.class)
                    .setParameter("userId" , userId)
                    .getResultList();

    }

    //신청한 수강 대상자 수를 +1 처리
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public void updateEnrollCount(Enrollment enrollment) {
        String jpql = "  UPDATE LectureSchedule SET enrollCount = enrollCount + 1" +
                "         WHERE id.lectureId = :lectureId" +
                "           AND id.lectureDy = :lectureDy" ;

        entityManager.createQuery(jpql)
                .setParameter("lectureId" , enrollment.getId().getLectureId())
                .setParameter("lectureDy" , enrollment.getId().getLectureDy())
                .executeUpdate();

    }

    //수강 대상자 수 조회
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public long selectEnrollCount(Enrollment enrollment) {
        String jpql = "SELECT enrollCount FROM LectureSchedule " +
                "         WHERE id.lectureId = :lectureId" +
                "           AND id.lectureDy = :lectureDy" ;

        Long count = entityManager.createQuery(jpql , Long.class)
                .setParameter("lectureId" , enrollment.getId().getLectureId())
                .setParameter("lectureDy" , enrollment.getId().getLectureDy())
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getSingleResult();

        return count;
    }




}
