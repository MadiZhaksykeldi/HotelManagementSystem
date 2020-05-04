package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.model.PriceDetail;
import com.example.demo.model.Room;
import com.example.demo.service.utils.DateUtility;
import com.example.demo.repository.PriceDetailRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.response.Response;
import com.example.demo.response.ResponseBuilder;
import com.example.demo.service.RoomService;
import com.example.demo.service.helper.PriceCalculator;
import com.example.demo.service.helper.RoomValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {
    Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PriceDetailRepository priceDetailRepository;

    @Override
    public Response getRooms() {
        Response response;
        try {
            logger.debug("Getting the room details");
            List<Room> roomList = roomRepository.findAll();
            response = ResponseBuilder.buildSucessResponse(roomList);
            logger.debug("Successfully got the rooms : {}", roomList);
        } catch (Exception e) {
            logger.error("Error: ", e);
            response = ResponseBuilder.buildFailureResponse(e.getMessage());
        }
        return response;
    }

    @Override
    public Response calculatetPriceByType(String type, String fromDateInString, String toDateInString) {
        Response response;
        try {
            logger.debug("Getting the room price for type : {} and duration from : {}  till : {}", type,
                    fromDateInString, toDateInString);
            List<Room> roomList = roomRepository.findByType(type);

            getRoomValidator().validate(type, roomList);
            DateUtility.validateDate(fromDateInString, toDateInString);
            Room room = roomList.get(0);
            PriceDetail priceDetail = getPriceCalculator().calculate(room, fromDateInString, toDateInString,
                    priceDetailRepository);
            response = ResponseBuilder.buildSucessResponse(priceDetail);
            logger.debug("Response : {} ", response);
        } catch (Exception e) {
            logger.error("Error: ", e);
            response = ResponseBuilder.buildFailureResponse(e.getMessage());
        }
        return response;
    }

    public RoomValidator getRoomValidator() {
        return new RoomValidator();
    }

    public PriceCalculator getPriceCalculator() {
        return new PriceCalculator();
    }
}