package com.example.demo.exception;

public class InvalidDateException extends Exception {
    private static final long serialVersionUID = 4059110895741877128L;

    public InvalidDateException() {
        super("Invalid Date");
    }

    public InvalidDateException(String message) {
        super(message);
    }
}