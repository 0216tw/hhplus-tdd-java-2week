package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.EnrollmentId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId>  , EnrollmentRepositoryCustom {
    List<Enrollment> findByIdUserId(Long userId);

}
