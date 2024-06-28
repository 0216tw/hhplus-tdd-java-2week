package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.Enrollment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepositoryCustom {

    public List<Enrollment> findByLectureIdAndLectureDy(long lectureId , String lectureDy);

} //git add
