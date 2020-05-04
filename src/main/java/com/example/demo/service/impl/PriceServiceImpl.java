package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.model.PriceDetail;
import com.example.demo.model.Room;
import com.example.demo.service.utils.DateUtility;
import com.example.demo.repository.PriceDetailRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.response.ResponseBuilder;
import com.example.demo.service.PriceService;
import com.example.demo.service.helper.RoomValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.response.Response;

@Service
public class PriceServiceImpl implements PriceService {
    Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);

    @Autowired
    private PriceDetailRepository priceDetailRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Response<?> addPriceDetails(PriceDetail priceDetail) {
        Response<?> response;
        try {
            DateUtility.validateDate(priceDetail.getFromDate(), priceDetail.getToDate());
            List<Room> roomList = roomRepository.findByType(priceDetail.getRoomType());
            getRoomValidator().validate(priceDetail.getRoomType(), roomList);
            Room room = roomRepository.findById(priceDetail.getRoomId());
            getRoomValidator().validate(room, priceDetail.getRoomId());
            PriceDetail priceDetails = priceDetailRepository.save(priceDetail);
            response = ResponseBuilder.buildSucessResponse(priceDetails);
        } catch (Exception exception) {
            logger.error("Error :", exception);
            response = ResponseBuilder.buildFailureResponse(exception.getMessage());
        }
        return response;
    }

    public RoomValidator getRoomValidator() {
        return new RoomValidator();
    }
}