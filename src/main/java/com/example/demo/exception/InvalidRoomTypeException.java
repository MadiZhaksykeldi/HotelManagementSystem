package com.example.demo.exception;

public class InvalidRoomTypeException extends Exception {
    private static final long serialVersionUID = 4079096994929673279L;

    public InvalidRoomTypeException() {
        super("Invalid Room Type");
    }
}
