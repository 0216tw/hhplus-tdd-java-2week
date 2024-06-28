package com.plusbackend.week2.validation;

public class UserIdValidator {

    public static boolean isValid(long userId) {

        if(userId <= 0) {
            return false ; //적절하지 않은 수치
        }

        return true;
    }
}
