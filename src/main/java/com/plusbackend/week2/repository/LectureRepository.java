package com.plusbackend.week2.repository;

import com.plusbackend.week2.domain.Lecture;
import com.plusbackend.week2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    //추가적인 메서드가 필요하면 여기에 선언을 할 수 있음
}
