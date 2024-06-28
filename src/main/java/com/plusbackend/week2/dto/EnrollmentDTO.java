package com.plusbackend.week2.dto;

import com.plusbackend.week2.validation.YyyymmddValidateAnnotation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EnrollmentDTO {

    @NotNull
    @Min(value = 1 , message="사용자ID는 최소 1이상의 값이어야 합니다.")
    private long userId; //사용자ID

    @NotNull
    @Min(value = 1 , message="강의ID는 최소 1이상의 값이어야 합니다.")
    private long lectureId; //강의 ID

    @NotBlank
    @YyyymmddValidateAnnotation
    private String lectureDy; //강의 일자 (날짜로 검증 필요) TODO

}
