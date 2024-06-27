package com.plusbackend.week2.controller;

import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.dto.UserDTO;
import com.plusbackend.week2.service.EnrollmentService;
import com.plusbackend.week2.service.LectureService;
import com.plusbackend.week2.validation.UserIdValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/lectures")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/apply")
    public ResponseEntity<ResponseDTO> apply(@Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        ResponseDTO responseDTO = enrollmentService.applylecture(enrollmentDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/application/{userId}")
    public ResponseEntity<ResponseDTO> getEnrollmentStatus(@PathVariable(value = "userId") long userId) {

        UserIdValidator.isValid(userId);
        ResponseDTO responseDTO = lectureService.getEnrollmentCompleteLecture(userId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> selectLectures(@PathVariable(value="userId") long userId) {
        UserIdValidator.isValid(userId);
        ResponseDTO responseDto = lectureService.getAvailableLecture(userId);
        return ResponseEntity.ok(responseDto);
    }
}
