package com.example.demo.service.helper;

import com.example.demo.model.Room;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomServiceHelper {
    Logger logger = LoggerFactory.getLogger(RoomServiceHelper.class);

    public List<Room> calculateRating(List<Room> roomList) {
        logger.debug("Setting the rating of the room : {}", roomList);
        Map<String, Float> ratingMap = new HashMap<>();
        float rating = 0;
        for (Room room : roomList) {
            if (ratingMap.containsKey(room.getType())) {
                rating = (ratingMap.get(room.getType()) + room.getRating()) / 2;
            }
            ratingMap.put(room.getType(), rating);
        }
        for (Room room : roomList) {
            room.setRating(ratingMap.get(room.getType()));
        }
        return roomList;
    }
}