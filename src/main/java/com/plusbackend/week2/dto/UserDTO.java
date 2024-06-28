package com.plusbackend.week2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UserDTO {

    @NotNull
    @Min(value = 1 , message="사용자ID는 최소 1이상의 값이어야 합니다.")
    private long userId;
    private String userName;
    private String phone;

}
