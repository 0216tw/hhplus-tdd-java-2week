package com.plusbackend.week2.unit.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plusbackend.week2.ApiControllerAdvice;
import com.plusbackend.week2.Week2Application;
import com.plusbackend.week2.controller.LectureController;
import com.plusbackend.week2.service.LectureService;
import com.plusbackend.week2.service.impl.LectureServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LectureController.class)
public class LectureControllerBase {

    /*
     * LectureController에 대한 Test Code 입니다.

     * 테스트 목록
     * -수강 신청 API :              POST /lectures/apply
     * -수강 신청 완료 여부 조회 API : GET /lectures/application/{userId}
     * -수강 선택 API :              GET /lectures
     */

     /*
        수강 신청 API 테스트 => LectureControllerApplyApiTest.java
        수강 신청 완료 여부 조회 API 테스트 => LectureControllerApplyCompleteApiTest.java
        수강 선택 API 테스트 => LectureControllerSelectApiTest.java
     */

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected LectureService lectureService;

    @BeforeEach
    protected void setUp() {

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    protected void close() {

    }

}
