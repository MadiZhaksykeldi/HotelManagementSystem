package com.example.demo.exception;

public class NoAvailableRoomException extends Exception {
    private static final long serialVersionUID = -971705421507493007L;

    public NoAvailableRoomException() {
        super("Room was not available to book");
    }
}