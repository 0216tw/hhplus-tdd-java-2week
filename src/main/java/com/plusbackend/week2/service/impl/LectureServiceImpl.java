package com.plusbackend.week2.service.impl;

import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.EnrollmentId;
import com.plusbackend.week2.domain.LectureSchedule;
import com.plusbackend.week2.domain.LectureScheduleId;
import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.LectureScheduleDTO;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import com.plusbackend.week2.exception.BusinessException;
import com.plusbackend.week2.repository.EnrollmentRepository;
import com.plusbackend.week2.repository.LectureScheduleRepository;
import com.plusbackend.week2.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class LectureServiceImpl implements LectureService {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    LectureScheduleRepository lectureScheduleRepository;

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

        //현재 수강 인원 조회 (일단 30명 고정)
        List<Enrollment> enrollments = enrollmentRepository.findByLectureIdAndLectureDy(lectureId , lectureDy);

        if(enrollments.size() >= 30) {
            throw new BusinessException(ResponseMessage.MAXIMUM_CAPACITY_EXCEEDED.getMessage());
        }


        enrollmentRepository.save(enrollment);
        return new ResponseDTO("200" , ResponseMessage.ENROLLMENT_SUCCESS.getMessage());
    }



    @Override
    public ResponseDTO getEnrollmentCompleteLecture(long userId) {
        return null;
    }

    @Override
    public ResponseDTO getAvailableLecture(long userId) {
        return null;
    }


}
