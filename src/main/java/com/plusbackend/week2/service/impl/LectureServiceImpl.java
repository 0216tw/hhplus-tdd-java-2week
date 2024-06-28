package com.plusbackend.week2.service.impl;

import com.plusbackend.week2.domain.*;
import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.LectureScheduleDTO;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import com.plusbackend.week2.exception.BusinessException;
import com.plusbackend.week2.repository.EnrollmentHistoryRepository;
import com.plusbackend.week2.repository.EnrollmentRepository;
import com.plusbackend.week2.repository.LectureRepository;
import com.plusbackend.week2.repository.LectureScheduleRepository;
import com.plusbackend.week2.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class LectureServiceImpl implements LectureService {


    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    LectureScheduleRepository lectureScheduleRepository;


    @Override
    public ResponseDTO getEnrollmentCompleteLecture(long userId) {
        List<Enrollment> data = enrollmentRepository.findByIdUserId(userId);
        return new ResponseDTO("200" , ResponseMessage.SEARCH_OK.getMessage() , data) ;
    }

    @Override
    public ResponseDTO getAvailableLecture(long userId) {
        List<LectureSchedule> data = lectureScheduleRepository.getAvailableLectures(userId);
        return new ResponseDTO("200" , ResponseMessage.SEARCH_OK.getMessage() , data);
    }


}
