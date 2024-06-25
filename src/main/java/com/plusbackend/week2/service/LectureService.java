package com.plusbackend.week2.service;

import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.LectureScheduleDTO;
import com.plusbackend.week2.dto.ResponseDTO;

public interface LectureService {

    /*
    * 1. 특강을 신청한다.
    * 2. 특강 신청 여부를 조회한다.
    * 3. 신청가능한 특강 목록을 조회한다.
    * */

    public ResponseDTO applylecture(EnrollmentDTO enrollmentDTO); //특강을 신청한다.
    public ResponseDTO getEnrollmentCompleteLecture(long userId); //특강 신청 완료 여부를 확인한다. (신청완료된 특강 정보만 출력)
    public ResponseDTO getAvailableLecture(long userId); //신청가능한 특강 목록을 반환한다.

}
