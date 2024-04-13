package com.madhan65.emailverification.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
