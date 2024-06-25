package com.plusbackend.week2.unit.service;


import com.plusbackend.week2.domain.*;
import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import com.plusbackend.week2.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
* 수강 신청 기능을 테스트하는 코드입니다
*/
public class ApplyLectureTest extends LectureServiceBase {

    /*
     * 실패 케이스
     * [1] 요청한 강의 일정이 없는 경우
     * [2] 이미 수강이 된 경우
     * [3] 수강 신청 인원이 이미 30명 이상인 경우
     *  동시성 테스트는 통합테스트에서 진행
     *  */
    @Test
    public void 수강_신청에_성공한_경우() {

        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623");
        ResponseDTO expected = new ResponseDTO("200" , ResponseMessage.ENROLLMENT_SUCCESS.getMessage());

        //when
        when(lectureScheduleRepository.findById(any(LectureScheduleId.class))).thenReturn(Optional.of(new LectureSchedule())); //강의 존재함
        when(enrollmentRepository.findById(any(EnrollmentId.class))).thenReturn(Optional.empty());
        when(enrollmentRepository.findByLectureIdAndLectureDy(1L , "20240623")).thenReturn(new ArrayList<>());
        when(enrollmentRepository.save(any())).thenReturn(any());
        ResponseDTO responseDTO = lectureService.applylecture(enrollmentDTO);

        //then
        Assertions.assertThat(responseDTO.getCode()).isEqualTo(expected.getCode());
        Assertions.assertThat(responseDTO.getMessage()).isEqualTo(expected.getMessage());
    }

    @Test
    public void 수강신청하려는_수강일정이_없는경우() {

        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623");

        //when
        when(lectureScheduleRepository.findById(any(LectureScheduleId.class))).thenReturn(Optional.empty()); //강의가 없음
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            lectureService.applylecture(enrollmentDTO);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(ResponseMessage.NO_SUCH_LECTURE_SCHEDULE.getMessage());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
        verify(enrollmentRepository, never()).findById(any(EnrollmentId.class));
    }


    @Test
    public void 이미_수강신청이_완료된_경우() {

        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623");
        EnrollmentId enrollmentId = new EnrollmentId(enrollmentDTO.getUserId() , enrollmentDTO.getLectureId() , enrollmentDTO.getLectureDy());
        Enrollment enrollment = new Enrollment(enrollmentId);

        //when
        when(lectureScheduleRepository.findById(any(LectureScheduleId.class))).thenReturn(Optional.of(new LectureSchedule())); //강의 존재함
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment)); //이미 강의 신청 했음

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            lectureService.applylecture(enrollmentDTO);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(ResponseMessage.ALREADY_ENROLLED.getMessage());
        verify(lectureScheduleRepository , times(1)).findById(any(LectureScheduleId.class));
        verify(enrollmentRepository, times(1)).findById(any(EnrollmentId.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void 신청_시점에_인원이_이미_30명인_경우() {
        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623");
        EnrollmentId enrollmentId = new EnrollmentId(enrollmentDTO.getUserId() , enrollmentDTO.getLectureId() , enrollmentDTO.getLectureDy());
        Enrollment enrollment = new Enrollment(enrollmentId);

        List<Enrollment> dummy30Enrollments = new ArrayList<>();
        for(int i = 0 ; i<30; i++) {
            dummy30Enrollments.add(enrollment);
        }

        //when
        when(lectureScheduleRepository.findById(any(LectureScheduleId.class))).thenReturn(Optional.of(new LectureSchedule())); //강의 존재함
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());
        when(enrollmentRepository.findByLectureIdAndLectureDy(1L , "20240623")).thenReturn(dummy30Enrollments); //이미 강의 신청 했음

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            lectureService.applylecture(enrollmentDTO);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(ResponseMessage.MAXIMUM_CAPACITY_EXCEEDED.getMessage());
        verify(lectureScheduleRepository , times(1)).findById(any(LectureScheduleId.class));
        verify(enrollmentRepository, times(1)).findById(any(EnrollmentId.class));
        verify(enrollmentRepository, times(1)).findByLectureIdAndLectureDy(anyLong() , anyString());

        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }
}
