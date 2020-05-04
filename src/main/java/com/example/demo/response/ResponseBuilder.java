package com.example.demo.response;

import com.example.demo.constants.Constants;

public class ResponseBuilder {
    private ResponseBuilder() {

    }

    public static <T> Response<T> buildSucessResponse(T message) {
        Response<T> response = new Response<>();
        response.setMessage(message);
        response.setStatus(Constants.SUCCESS);
        return response;
    }

    public static <T> Response<T> buildFailureResponse(T message) {
        Response<T> response = new Response<>();
        response.setMessage(message);
        response.setStatus(Constants.FAILURE);
        return response;
    }
}