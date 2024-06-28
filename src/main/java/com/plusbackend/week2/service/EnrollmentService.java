package com.plusbackend.week2.service;

import com.plusbackend.week2.dto.EnrollmentDTO;
import com.plusbackend.week2.dto.ResponseDTO;

public interface EnrollmentService {
    public ResponseDTO applylecture(EnrollmentDTO enrollmentDTO);
}
