package com.plusbackend.week2.unit.service;

import com.plusbackend.week2.domain.*;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/*
 * 수강 신청 가능한 목록을 조회하는 테스트 코드 입니다.
 */
public class GetAvailableLectureTest extends LectureServiceBase {


    /*
     * 실패 케이스
     * [1] userId의 범위가 적절하지 않은 경우 -> Controller 단에서 체크됨
     */

    @Test
    public void 수강_신청_가능한_목록_조회_성공() {
        //given
        //lecture목록 (수강일자 , 강의명 , 일정)
        List<LectureSchedule> lectureSchedules = new ArrayList<>();
        List<Enrollment> userEnrolled = new ArrayList<>();

        //강의 2개
        Lecture lecture1 = new Lecture(1L , "강의1" , 30 , "강의설명1");
        Lecture lecture2 = new Lecture(2L , "강의2" , 30 , "강의설명2");
        Lecture lecture3 = new Lecture(3L , "강의3" , 30 , "강의설명3");

        //복합키 설정 (lecture_id , lecture_dy)
        LectureScheduleId lectureScheduleId1 = new LectureScheduleId(1L , "20240610"); //들을거
        LectureScheduleId lectureScheduleId2 = new LectureScheduleId(1L , "20240611");
        LectureScheduleId lectureScheduleId3 = new LectureScheduleId(2L , "20240610"); //들을거
        LectureScheduleId lectureScheduleId4 = new LectureScheduleId(2L , "20240611"); //들을거
        LectureScheduleId lectureScheduleId5 = new LectureScheduleId(3L , "20240610");

        //강의 일정 세팅
        lectureSchedules.add(new LectureSchedule(lectureScheduleId1 , lecture1 , new Date() , new Date()));
        lectureSchedules.add(new LectureSchedule(lectureScheduleId2 , lecture1 , new Date() , new Date()));
        lectureSchedules.add(new LectureSchedule(lectureScheduleId3 , lecture2 , new Date() , new Date()));
        lectureSchedules.add(new LectureSchedule(lectureScheduleId4 , lecture2 , new Date() , new Date()));
        lectureSchedules.add(new LectureSchedule(lectureScheduleId5 , lecture3 , new Date() , new Date()));

        //사용자 강의 정보 세팅
        userEnrolled.add(new Enrollment(new EnrollmentId(1L , 1L , "20240610")));
        userEnrolled.add(new Enrollment(new EnrollmentId(1L , 2L , "20240610")));
        userEnrolled.add(new Enrollment(new EnrollmentId(1L , 2L , "20240611")));

        // 사용자 강의 정보에 없는 강의 일정 추출
        Set<LectureScheduleId> userEnrolledScheduleIds = userEnrolled.stream()
                .map(enrollment -> new LectureScheduleId(enrollment.getId().getLectureId(), enrollment.getId().getLectureDy()))
                .collect(Collectors.toSet());

        List<LectureSchedule> schedulesNotEnrolled = lectureSchedules.stream()
                .filter(schedule -> !userEnrolledScheduleIds.contains(schedule.getId()))
                .collect(Collectors.toList());

        when(lectureScheduleRepository.getAvailableLectures(1L)).thenReturn(schedulesNotEnrolled);

        ResponseDTO responseDTO = lectureService.getAvailableLecture(1L);

        //then
        verify(lectureScheduleRepository , times(1)).getAvailableLectures(anyLong());
        Assertions.assertThat(responseDTO.getCode()).isEqualTo("200");
        Assertions.assertThat(responseDTO.getMessage()).isEqualTo(ResponseMessage.SEARCH_OK.getMessage());
        Assertions.assertThat(responseDTO).isNotNull();

        //수강 가능한 대상 목록
        List<LectureSchedule> data = (List<LectureSchedule>)responseDTO.getData();
        for(LectureSchedule lectureSchedule : data) {
            System.out.println(lectureSchedule.getLecture().getLectureId());
            System.out.println(lectureSchedule.getLecture().getLectureName());
            System.out.println(lectureSchedule.getId().getLectureDy());

        }

        Assertions.assertThat(data.get(0).getLecture().getLectureName()).isEqualTo(schedulesNotEnrolled.get(0).getLecture().getLectureName());
        Assertions.assertThat(data.get(1).getLecture().getLectureName()).isEqualTo(schedulesNotEnrolled.get(1).getLecture().getLectureName());

    }

}
