package com.plusbackend.week2.unit.service;


import com.plusbackend.week2.domain.Enrollment;
import com.plusbackend.week2.domain.EnrollmentId;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import org.assertj.core.api.Assertions;
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

        when(enrollmentRepository.findByIdUserId(1L)).thenReturn(userEnrolled);

        ResponseDTO responseDTO = lectureService.getEnrollmentCompleteLecture(1L);

        Assertions.assertThat(responseDTO.getCode()).isEqualTo("200");
        Assertions.assertThat(responseDTO.getMessage()).isEqualTo(ResponseMessage.SEARCH_OK.getMessage());
        Assertions.assertThat(responseDTO.getData()).isNotNull();

        List<Enrollment> data = (List<Enrollment>)responseDTO.getData();

        Assertions.assertThat(data.size()).isEqualTo(3);
        Assertions.assertThat(data.get(0).getId().getLectureId()).isEqualTo(userEnrolled.get(0).getId().getLectureId());
        Assertions.assertThat(data.get(1).getId().getLectureDy()).isEqualTo(userEnrolled.get(1).getId().getLectureDy());



    }

}
