package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.LectureSchedule;
import com.plusbackend.week2.domain.LectureScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, LectureScheduleId> {

}