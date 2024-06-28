package com.plusbackend.week2.repository.impl;

import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.LectureSchedule;
import com.plusbackend.week2.repository.EnrollmentRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.hibernate.LockMode;

import java.util.List;

public class EnrollmentRepositoryImpl implements EnrollmentRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Enrollment> findByLectureIdAndLectureDy(long lectureId, String lectureDy) {
        String jpql = "SELECT A FROM Enrollment A WHERE A.id.lectureId = :lectureId AND A.id.lectureDy = :lectureDy";

        return entityManager.createQuery(jpql , Enrollment.class)
                .setParameter("lectureId" , lectureId)
                .setParameter("lectureDy" , lectureDy)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
    }


}
