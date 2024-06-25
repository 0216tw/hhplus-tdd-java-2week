package com.plusbackend.week2.unit.controller;


import com.plusbackend.week2.dto.*;
import com.plusbackend.week2.enums.ResponseMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
* 수강 신청 API 테스트
*/
public class LectureControllerApplyApiTest extends LectureControllerBase {

    /*
    *  실패 케이스
    *  [1] 잘못된 URL로 요청한 경우 -> 404 response  (이건 다른 경우에도 똑같으니까 봐서 리팩토링 해야겠다)
    *  [2] 잘못된 http 요청 메소드를 사용한 경우 -> 405 method not allowed (이것도 다른 경우에도 똑감음)
    *  [3] 잘못된 필드명으로 요청을 한 경우는?  -> 400 (이것도 다른 경우에 포함을 하면?)
    *  [4] 파라미터 임계값
    *      [4-1] 필수값이 없는 경우 (userId , lectureId , lectureDy) -> 400 response
    *      [4-2] 자료형이 부적합한 경우 (long , long , String) -> 400 response
    *      [4-3] 비정상적인 값이 들어오면 어떻게 되지? 예) userId에 1111111111111111111111111111111111111111111111111111111111 -> 400 response
    *      [4-4] lectureDy의 날짜 유효성이 올바르지 않을 경우 -> 400 response
    *
    *  누락한 테스트 없음 (리팩토링 필요)
    *  */


    @Test
    public void 수강_신청에_성공시_정상응답_리턴() throws Exception {

        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623");
        String enrollmentJson = objectMapper.writeValueAsString(enrollmentDTO);
        ResponseDTO responseDTO = new ResponseDTO("200" , ResponseMessage.ENROLLMENT_SUCCESS.getMessage());

        //when
        doAnswer(invocation -> {
            EnrollmentDTO dto = invocation.getArgument(0);
            // Custom logic here if needed
            return responseDTO; // return the mock responseDTO
        }).when(lectureService).applylecture(any(EnrollmentDTO.class));

        MvcResult result = mockMvc.perform(post("/lectures/apply")

                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(enrollmentJson))
                        .andReturn();

        // Get the response content as a string
        String responseContent = new String(result.getResponse().getContentAsByteArray(), "UTF-8");

        Assertions.assertThat(responseContent).isEqualTo("{\"code\":\"200\",\"message\":\"수강신청이 완료되었습니다.\"}");

        // Print the response content
        System.out.println("Response Content: " + responseContent);
    }


    @Test
    public void 잘못된_URL로_요청을_한경우_404_error_반환() throws Exception {

        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623");
        String enrollmentJson = objectMapper.writeValueAsString(enrollmentDTO);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/lectures/invalid-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson));

        //then
//        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("올바르지 않은 경로입니다."));
    }


    @Test
    public void 잘못된_HTTP_Method로_요청한_경우_405_error_반환() throws Exception {

        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623");
        String enrollmentJson = objectMapper.writeValueAsString(enrollmentDTO);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/lectures/apply") //post인데 일부러 잘못된 get을 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson));

        //then
        resultActions.andExpect(status().isMethodNotAllowed());
        resultActions.andExpect(content().string("올바르지 않은 HTTP 메서드입니다."));
    }

    @Test
    public void 잘못된_필드명으로_요청한_경우_400_error_반환() throws Exception {

        //given (잘못된 입력 필드 처리) userId 가 아니라 u로 시도
        String enrollmentJson = """
                {"u":1,"lectureId":1,"lectureDy":"20240623"}
                """;

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/lectures/apply") //post인데 일부러 잘못된 get을 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson));

        //then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("잘못된 요청입니다. 요청 데이터를 확인해주세요."));

    }

    @Test
    public void 필수_입력값이_누락된_경우_400_error_반환() throws Exception {

        //given (필수값인 lectureDy가 누락된 경우)
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "");
        String enrollmentJson = objectMapper.writeValueAsString(enrollmentDTO);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson));

        //then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("잘못된 요청입니다. 요청 데이터를 확인해주세요."));

    }

    @Test
    public void 자료형이_부적합한_경우_400_error_반환() throws Exception {

        //given (long인 userId에 "hello"를 입력)
        String enrollmentJson = """
                {"userId":"hello","lectureId":1,"lectureDy":"20240623"}
                """;

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson));

        //then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("잘못된 요청입니다. 요청 데이터를 확인해주세요."));
    }

    @Test
    public void 비정상적인_임계값이_들어온_경우_400_error_반환() throws Exception {

        //given (long인값이 범위를 넘어서는 값을 넣으면 Json Parse error 가 발생)
        String enrollmentJson = """
                {"userId":7654555555555555534636352312353348734566387,"lectureId":1,"lectureDy":"20240623"}
                """;

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson));

        //then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("잘못된 요청입니다. 요청 데이터를 확인해주세요."));
    }


    @Test
    public void 사용자id와_강의id가_0이하로_입력된_경우() throws Exception {

        //given
        //given (필수값인 lectureDy가 누락된 경우)
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(-12345L , 1L , "20240623");
        String enrollmentJson = objectMapper.writeValueAsString(enrollmentDTO);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson));

        //then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("잘못된 요청입니다. 요청 데이터를 확인해주세요."));
    }

    @Test
    public void 입력받은_날짜가_유효한_날짜가_아닌경우() throws Exception {


        //given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L , 1L , "20240623123");
        String enrollmentJson = objectMapper.writeValueAsString(enrollmentDTO);

        //when
        mockMvc.perform(post("/lectures/apply")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(enrollmentJson))
        //then
                     .andExpect(status().isBadRequest())
                     .andExpect(content().string("잘못된 요청입니다. 요청 데이터를 확인해주세요."));

        //given
        enrollmentDTO = new EnrollmentDTO(1L , 1L , "20249999");
        enrollmentJson = objectMapper.writeValueAsString(enrollmentDTO);

        //when
        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("잘못된 요청입니다. 요청 데이터를 확인해주세요."));

    }


}
