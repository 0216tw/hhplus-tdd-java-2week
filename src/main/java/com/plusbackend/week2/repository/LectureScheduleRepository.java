package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.LectureSchedule;
import com.plusbackend.week2.domain.LectureScheduleId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, LectureScheduleId> , LectureScheduleRepositoryCustom{
}