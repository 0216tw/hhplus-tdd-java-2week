package com.plusbackend.week2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = YyyymmddValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface YyyymmddValidateAnnotation {
    String message() default "유효하지 않은 날짜입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
