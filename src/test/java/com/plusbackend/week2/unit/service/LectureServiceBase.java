package com.plusbackend.week2.unit.service;

import com.plusbackend.week2.repository.EnrollmentRepository;
import com.plusbackend.week2.repository.LectureScheduleRepository;
import com.plusbackend.week2.repository.UserRepository;
import com.plusbackend.week2.service.impl.LectureServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LectureServiceBase {

    /*
     * LectureService에 대한 Test Code 입니다.
     *
     * 테스트 목록
      public ResponseDTO applylecture(); //특강을 신청한다.
      public ResponseDTO isEnrollmentCompleted(); //특강 신청 완료 여부를 확인한다.
      public ResponseDTO getAvailableLecture(); //신청가능한 특강 목록을 반환한다.
     */

    @InjectMocks
    public LectureServiceImpl lectureService;

    @Mock
    public EnrollmentRepository enrollmentRepository;

    @Mock
    LectureScheduleRepository lectureScheduleRepository;

    @BeforeEach
    public void setUp() {
        lectureService = new LectureServiceImpl();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() {

    }

}
