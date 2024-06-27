package com.plusbackend.week2.service.impl;

import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.EnrollmentHistory;
import com.plusbackend.week2.domain.EnrollmentId;
import com.plusbackend.week2.domain.LectureScheduleId;
import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import com.plusbackend.week2.exception.BusinessException;
import com.plusbackend.week2.repository.*;
import com.plusbackend.week2.service.EnrollmentService;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class EnrollmentServiceImpl  implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    LectureScheduleRepository lectureScheduleRepository;

    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    private EnrollmentHistoryRepository enrollmentHistoryRepository;

    @Override
    @Transactional
    public ResponseDTO applylecture(EnrollmentDTO enrollmentDTO) {

        long userId = enrollmentDTO.getUserId();
        long lectureId = enrollmentDTO.getLectureId();
        String lectureDy = enrollmentDTO.getLectureDy();

        EnrollmentId enrollmentId = new EnrollmentId(userId , lectureId , lectureDy);
        Enrollment enrollment = new Enrollment(enrollmentId);
        LectureScheduleId lectureScheduleId = new LectureScheduleId(lectureId, lectureDy);

        //강의일정이 존재하는지 확인
        lectureScheduleRepository.findById(lectureScheduleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.NO_SUCH_LECTURE_SCHEDULE.getMessage()));

        //이미 신청했는지 확인
        if(enrollmentRepository.findById(enrollmentId).isPresent()) {
            throw new BusinessException(ResponseMessage.ALREADY_ENROLLED.getMessage());
        }

        //현재 수강 인원 조회 (비관 락)
        long enrollCount = lectureScheduleRepository.selectEnrollCount(enrollment);
        System.out.println("현재 수강 인원: " + enrollCount);
        if (enrollCount >= 30) {
            throw new BusinessException(ResponseMessage.MAXIMUM_CAPACITY_EXCEEDED.getMessage());
        }

        lectureScheduleRepository.updateEnrollCount(enrollment);
        enrollmentRepository.save(enrollment);
        enrollmentHistoryRepository.save( //이력적재
                    new EnrollmentHistory(
                            enrollment.getId().getUserId(),
                            enrollment.getId().getLectureId(),
                            enrollment.getId().getLectureDy(),
                            "reg" //취소는 cnl
                    ));


        return new ResponseDTO("200" , ResponseMessage.ENROLLMENT_SUCCESS.getMessage());
    }


}
