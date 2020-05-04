package com.example.demo.service.helper;

import java.util.List;

import com.example.demo.exception.InvalidRoomTypeException;
import com.example.demo.model.Room;
import com.example.demo.service.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoomValidator {
    Logger logger = LoggerFactory.getLogger(RoomServiceHelper.class);

    public void validate(Room room, String type) throws InvalidRoomTypeException {
        if (CommonUtils.isEmptyOrNullObject(room)) {
            logger.debug("Room null for {} ", type);
            throw new InvalidRoomTypeException();
        }
    }

    public void validate(String type, List<Room> roomList) throws InvalidRoomTypeException {
        if (CommonUtils.isEmptyOrNullCollection(roomList)) {
            logger.debug("Room list null for {} ", type);
            throw new InvalidRoomTypeException();
        }
    }

    public void validate(Room room, long id) throws InvalidRoomTypeException {
        if (CommonUtils.isEmptyOrNullObject(room)) {
            logger.debug("Room null for id : {} ", id);
            throw new InvalidRoomTypeException();
        }
    }
}