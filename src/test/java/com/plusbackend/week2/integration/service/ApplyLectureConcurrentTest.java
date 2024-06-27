package com.plusbackend.week2.integration.service;


import com.plusbackend.week2.domain.*;
import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import com.plusbackend.week2.exception.BusinessException;
import com.plusbackend.week2.repository.EnrollmentRepository;
import com.plusbackend.week2.repository.LectureRepository;
import com.plusbackend.week2.repository.LectureScheduleRepository;
import com.plusbackend.week2.service.EnrollmentService;
import com.plusbackend.week2.service.LectureService;
import com.plusbackend.week2.unit.service.LectureServiceBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
* 수강 신청의 동시성을 테스트하는 코드입니다.
*/

@SpringBootTest
public class ApplyLectureConcurrentTest {

    @Autowired
    EnrollmentService enrollmentService;

    @Autowired
    LectureService lectureService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    LectureScheduleRepository lectureScheduleRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Test
    public void 비관락으로_테스트() throws InterruptedException {

        //given
        Lecture lecture1 = new Lecture(1L , "플러스백엔드5기특강"  , 30L , "이 강의는 말이죠..~~");
        Lecture lecture2 = new Lecture(2L , "플러스프론트엔트2기특강" , 30L , "이 강의는 프론트란 말이죠~~");

        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        List<Lecture> lectures = lectureRepository.findAll();

        long lectureId1 = lectures.get(0).getLectureId();
        long lectureId2 = lectures.get(1).getLectureId();

        LectureScheduleId lectureScheduleId1 = new LectureScheduleId(lectureId1 , "20240624");
        LectureScheduleId lectureScheduleId2 = new LectureScheduleId(lectureId1 , "20240625");
        LectureScheduleId lectureScheduleId3 = new LectureScheduleId(lectureId2 , "20240626");
        LectureScheduleId lectureScheduleId4 = new LectureScheduleId(lectureId2 , "20240627");

        Date startDate = Date.from(LocalDateTime.of(2024, Month.JUNE , 25 , 9 , 00).atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDateTime.of(2024, Month.JUNE , 25 , 11 , 00).atZone(ZoneId.systemDefault()).toInstant());

        LectureSchedule lectureSchedule1 = new LectureSchedule(lectureScheduleId1  , lectures.get(0) , startDate , endDate  );
        LectureSchedule lectureSchedule2 = new LectureSchedule(lectureScheduleId2  , lectures.get(0), startDate , endDate );
        LectureSchedule lectureSchedule3 = new LectureSchedule(lectureScheduleId3   , lectures.get(1), startDate , endDate );
        LectureSchedule lectureSchedule4 = new LectureSchedule(lectureScheduleId4  , lectures.get(1), startDate , endDate );


        lectureScheduleRepository.save(lectureSchedule1);
        lectureScheduleRepository.save(lectureSchedule2);
        lectureScheduleRepository.save(lectureSchedule3);
        lectureScheduleRepository.save(lectureSchedule4);

        ExecutorService executor = Executors.newFixedThreadPool(10); //최대 스레드 10개
        CountDownLatch doneSignal = new CountDownLatch(100);
        for(int i=0; i<100; i++) {
            EnrollmentDTO enrollmentDTO = new EnrollmentDTO(i+1L , 1L , "20240624");

            executor.submit( () -> {
                try {
                    enrollmentService.applylecture(enrollmentDTO);
                }  catch (Exception e) {
                    System.out.println("now i => " + enrollmentDTO.getUserId() + " ," + e.getMessage() );

                } finally {
                    doneSignal.countDown(); //작업량 끝날때마다 감소
                }
            });
        }
        doneSignal.await();
        executor.shutdown();


        List<Enrollment> enrollments = enrollmentRepository.findAll();
        Assertions.assertThat(enrollments.size()).isEqualTo(30);
    }
}
