package com.example.demo.service;

import com.example.demo.request.BookingRequest;
import com.example.demo.response.Response;

public interface BookingService {
    public Response bookRoom(BookingRequest bookingRequest);

    public Response checkAvailability(String type, String fromDate, String toDate);
}