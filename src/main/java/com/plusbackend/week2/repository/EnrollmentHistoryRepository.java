package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.EnrollmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentHistoryRepository extends JpaRepository<EnrollmentHistory , Long> {
}
