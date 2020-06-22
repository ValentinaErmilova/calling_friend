package com.company.exception;

// This exception is thrown when a user tries to add another nonexistent user to the contacts
public class UserNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "User with given phone number not found";

    public UserNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
}
