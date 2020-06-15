package com.company.exception;

public class UserException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "User with given phone number not found";

    public UserException(){
        super(DEFAULT_MESSAGE);
    }
}
