package com.example.demo.exception;

public class InvalidUserException extends Exception {
    private static final long serialVersionUID = -4002696843718742004L;

    public InvalidUserException() {
        super("Invalid user");
    }
}