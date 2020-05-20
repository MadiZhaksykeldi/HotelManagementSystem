package com.example.demo.controller;

import com.example.demo.response.Response;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/rooms")
@Api(value = "Manage the hotel room", description = "Used to manage the room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @ApiOperation(value = "Used to get all the rooms", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of Room and its details")})
    @GetMapping("/getRooms")
    public Response getRooms() {
        return roomService.getRooms();
    }

    @ApiOperation(value = "Used to the price of the room for the given duration", response = Response.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "1. Price details" + "\n\n2. Invalid Room type"
            + "\n\n3. Invalid Date")})
    @GetMapping("/getPrice/{type}")
    public Response getPrice(@PathVariable(value = "type", required = true) String type,
                             @RequestParam(value = "fromDate", required = true) String fromDate,
                             @RequestParam(value = "toDate", required = true) String toDate) {
        return roomService.calculatetPriceByType(type, fromDate, toDate);
    }
}