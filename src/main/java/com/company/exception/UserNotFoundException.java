package com.company.exception;

public class UserNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "User with given phone number not found";

    public UserNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
}
