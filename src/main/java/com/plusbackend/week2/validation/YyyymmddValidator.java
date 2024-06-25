package com.plusbackend.week2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class YyyymmddValidator implements ConstraintValidator<YyyymmddValidateAnnotation , String> {
    private static final String DATE_FORMAT = "yyyyMMdd";

    @Override
    public void initialize(YyyymmddValidateAnnotation constraintAnnotation) {
       // ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return false;

        if(value.length() != 8 || !value.matches("\\d{8}")) return false;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);

        try {
            sdf.parse(value);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}
