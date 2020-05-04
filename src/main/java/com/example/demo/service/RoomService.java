package com.example.demo.service;

import com.example.demo.response.Response;

public interface RoomService {
    public Response getRooms();

    public Response calculatetPriceByType(String type, String fromDate, String toDate);
}