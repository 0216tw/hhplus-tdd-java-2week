package com.plusbackend.week2.service;

import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.LectureScheduleDTO;
import com.plusbackend.week2.dto.ResponseDTO;
import org.springframework.stereotype.Service;


public interface LectureService {

    /*
    * 1. 특강을 신청한다. -> EnrollmentService로 이동
    * 2. 특강 신청한 목록을 출력한다.
    * 3. 신청가능한 특강 목록을 출력한다.
    * */

    //public ResponseDTO applylecture(EnrollmentDTO enrollmentDTO);
    public ResponseDTO getEnrollmentCompleteLecture(long userId); //특강 신청한 목록을 출력한다.
    public ResponseDTO getAvailableLecture(long userId); //신청가능한 특강 목록을 출력한다.

}
