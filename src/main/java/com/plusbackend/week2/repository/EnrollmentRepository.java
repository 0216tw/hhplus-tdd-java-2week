package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

    public List<Enrollment> findByLectureIdAndLectureDy(long lectureId , String lectureDy);
}
