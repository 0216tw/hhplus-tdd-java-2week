package com.plusbackend.week2;

import com.plusbackend.week2.dto.ResponseDTO;
import com.plusbackend.week2.enums.ResponseMessage;
import com.plusbackend.week2.exception.BusinessException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiControllerAdvice  {
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<String> handle404Exception(NoResourceFoundException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(ResponseMessage.NOT_FOUND.getMessage() , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handle405Exception(HttpRequestMethodNotSupportedException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(ResponseMessage.METHOD_NOT_ALLOWED.getMessage() , HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(ResponseMessage.BAD_REQUEST.getMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(ResponseMessage.BAD_REQUEST.getMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR); //이게 서버 오류가 맞나?
    }


    /*
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {

        return new ResponseEntity<>("서버 내부 오류가 발생했습니다." , HttpStatus.INTERNAL_SERVER_ERROR);
    }
    아 이놈이 문제였네
     */
}
