package com.plusbackend.week2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    @JsonProperty
    private String code;
    @JsonProperty
    private String message;
    @JsonProperty
    private T data;

    public ResponseDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
