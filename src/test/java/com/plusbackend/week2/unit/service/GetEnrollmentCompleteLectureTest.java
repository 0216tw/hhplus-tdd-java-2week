package com.plusbackend.week2.unit.service;


import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.EnrollmentId;
import com.plusbackend.week2.dto.ResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/*
* 사용자가 수강신청한 목록을 출력해준다.
*/
public class GetEnrollmentCompleteLectureTest extends LectureServiceBase {

    @Test
    public void 사용자가_신청한_수강목록_조회_성공() {

        List<Enrollment> userEnrolled = new ArrayList<>();

        userEnrolled.add(new Enrollment(new EnrollmentId(1L , 1L , "20240610")));
        userEnrolled.add(new Enrollment(new EnrollmentId(1L , 2L , "20240610")));
        userEnrolled.add(new Enrollment(new EnrollmentId(1L , 2L , "20240611")));

        when(enrollmentRepository.findAll()).thenReturn(userEnrolled);

        ResponseDTO responseDTO = lectureService.getEnrollmentCompleteLecture(1L);

        //리스트를 던질 수 있구나



    }

}
