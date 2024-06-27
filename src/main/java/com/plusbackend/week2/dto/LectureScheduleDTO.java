package com.plusbackend.week2.dto;

import com.plusbackend.week2.validation.YyyymmddValidateAnnotation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class LectureScheduleDTO {

    @NotNull
    @Min(value = 1 , message="강의ID는 최소 1이상의 값이어야 합니다.")
    private long lectureId ;

    @NotBlank
    @YyyymmddValidateAnnotation
    private String lectureDy;

    private Date startDate;
    private Date endDate ;

}
