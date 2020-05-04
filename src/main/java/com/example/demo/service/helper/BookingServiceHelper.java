package com.example.demo.service.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.exception.InvalidUserException;
import com.example.demo.model.BookingDetail;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.service.utils.CommonUtils;
import com.example.demo.repository.BookingDetailRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.BookingRequest;
import com.example.demo.response.Response;
import com.example.demo.response.ResponseBuilder;


public class BookingServiceHelper {
    Logger logger = LoggerFactory.getLogger(BookingServiceHelper.class);

    public Response frameResponse(String type, Date fromDate, Date toDate, List<BookingDetail> bookedDetailList) {
        if (CommonUtils.isEmptyOrNullCollection(bookedDetailList)) {
            return ResponseBuilder.buildSucessResponse(
                    type + " type room is avilable for the given duration : " + fromDate + " - " + toDate);
        } else {
            return ResponseBuilder
                    .buildFailureResponse(type + " type room is not avilable from: " + fromDate + " to " + toDate);
        }
    }

    public List<BookingDetail> fetchBookedDetails(List<Room> roomList, Date fromDate, Date toDate,
                                                  BookingDetailRepository bookingDetailsRepository) {
        List<BookingDetail> bookingDetailsForTheRoomType = new ArrayList<>();
        for (Room room : roomList) {
            List<BookingDetail> roomBookingList = bookingDetailsRepository.findByRoomIdAndDateRange(room.getId(),
                    fromDate, toDate);
            bookingDetailsForTheRoomType.addAll(roomBookingList);
        }
        return bookingDetailsForTheRoomType;
    }

    public BookingDetail buildBookingDetails(Date fromDate, Date toDate, User user, Room room) {
        BookingDetail bookingDetails = new BookingDetail();
        bookingDetails.setUserId(user.getUserId());
        bookingDetails.setFromDate(fromDate);
        bookingDetails.setToDate(toDate);
        bookingDetails.setUserName(user.getUserName());
        bookingDetails.setRoomId(room.getId());
        return bookingDetails;
    }

    public User getUser(BookingRequest bookingRequest, UserRepository userRepository) throws InvalidUserException {

        User user = userRepository.findByUserName(bookingRequest.getUserName());
        if (user == null) {
            logger.debug("invalid user " + bookingRequest.getUserName());
            throw new InvalidUserException();
        }
        return user;
    }
}