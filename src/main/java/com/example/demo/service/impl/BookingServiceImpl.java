package com.example.demo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.example.demo.exception.InvalidDateException;
import com.example.demo.exception.InvalidRoomTypeException;
import com.example.demo.exception.NoAvailableRoomException;
import com.example.demo.model.BookingDetail;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.service.utils.CommonUtils;
import com.example.demo.service.utils.DateUtility;
import com.example.demo.repository.BookingDetailRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.BookingRequest;
import com.example.demo.response.ResponseBuilder;
import com.example.demo.service.BookingService;
import com.example.demo.service.helper.BookingServiceHelper;
import com.example.demo.service.helper.RoomValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.response.Response;

@Service
public class BookingServiceImpl implements BookingService {
    Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingDetailRepository bookingDetailsRepository;

    private static Map<Long, Lock> roomLock = new WeakHashMap<>();
    private static Map<Long, String> fromDateDetailsMap = new WeakHashMap<>();
    private static Map<Long, String> toDateDetailsMap = new WeakHashMap<>();

    @Override
    public Response bookRoom(BookingRequest bookingRequest) {
        Response response;
        try {
            validateRoom(bookingRequest);
            User user = getBookingServiceHelper().getUser(bookingRequest, userRepository);
            Date fromDate = DateUtility.getDateFromString(bookingRequest.getFromDate());
            Date toDate = DateUtility.getDateFromString(bookingRequest.getToDate());
            DateUtility.validateDate(fromDate, toDate);
            BookingDetail bookingDetails = bookRoom(bookingRequest.getFromDate(), bookingRequest.getToDate(),
                    bookingRequest.getRoomType(), user);
            response = ResponseBuilder.buildSucessResponse(bookingDetails);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = ResponseBuilder.buildFailureResponse(e.getMessage());
        }
        return response;
    }

    private void validateRoom(BookingRequest bookingRequest) throws InvalidRoomTypeException {
        String roomType = bookingRequest.getRoomType();
        List<Room> roomList = roomRepository.findByType(roomType);
        getRoomValidator().validate(roomType, roomList);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingDetail bookRoom(String fromDate, String toDate, String roomType, User user)
            throws NoAvailableRoomException, InvalidDateException, InterruptedException {
        List<Room> roomList = roomRepository.findByType(roomType);
        Room availableRoom = getTheAvailableRoom(fromDate, toDate, roomList);
        if (CommonUtils.isEmptyOrNullObject(availableRoom)) {
            throw new NoAvailableRoomException();
        }

        BookingDetail bookingDetail = getBookingServiceHelper().buildBookingDetails(
                DateUtility.getDateFromString(fromDate), DateUtility.getDateFromString(toDate), user, availableRoom);
        return bookingDetailsRepository.save(bookingDetail);
    }

    private Room getTheAvailableRoom(String fromDateInString, String toDateInString, List<Room> roomList)
            throws InvalidDateException, InterruptedException {
        Room availableRoom = null;
        for (Room room : roomList) {
            availableRoom = checkForRoomAvailability(fromDateInString, toDateInString, room);
            if (!CommonUtils.isEmptyOrNullObject(availableRoom)) {
                break;
            }
        }
        return availableRoom;
    }

    private Room checkForRoomAvailability(String fromDateInString, String toDateInString, Room room)
            throws InvalidDateException, InterruptedException {
        Room roomToBook = null;
        Lock lock = getLock(fromDateInString, toDateInString, room.getId());
        if (lock.tryLock(2, TimeUnit.MINUTES)) {
            try {
                List<BookingDetail> bookingDetailList = bookingDetailsRepository.findByRoomIdAndDateRange(room.getId(),
                        DateUtility.getDateFromString(fromDateInString), DateUtility.getDateFromString(toDateInString));
                if (CommonUtils.isEmptyOrNullCollection(bookingDetailList)) {
                    roomToBook = room;
                }
            } catch (Exception e) {
                logger.error("Error: ", e);
            } finally {
                lock.unlock();
            }
        } else {
            roomToBook = null;
        }
        return roomToBook;
    }

    private Lock getLock(String fromDateInString, String toDateInString, long roomId) {
        boolean isAvailableInGivenRange = false;
        if (fromDateDetailsMap.containsKey(roomId) && toDateDetailsMap.containsKey(roomId)) {
            isAvailableInGivenRange = DateUtility.checkInRange(fromDateInString, toDateInString,
                    fromDateDetailsMap.get(roomId), toDateDetailsMap.get(roomId));
        }
        if (!roomLock.containsKey(roomId) && !isAvailableInGivenRange) {
            roomLock.put(roomId, new ReentrantLock());
            fromDateDetailsMap.put(roomId, fromDateInString);
            toDateDetailsMap.put(roomId, toDateInString);
        }
        return roomLock.get(roomId);
    }

    @Override
    public Response checkAvailability(String type, String fromDateInString, String toDateInString) {
        Response response;
        try {
            logger.debug("Checking the RoomAvailability for type: {} and for duration fromData : {} , toDate : {}",
                    type, fromDateInString, toDateInString);

            DateUtility.validateDate(fromDateInString, toDateInString);

            List<Room> roomList = roomRepository.findByType(type);

            if (CommonUtils.isEmptyOrNullCollection(roomList)) {
                throw new InvalidRoomTypeException();
            }

            Date fromDate = DateUtility.getDateFromString(fromDateInString);
            Date toDate = DateUtility.getDateFromString(toDateInString);

            List<BookingDetail> listOfBookingDetailsForTheRoomType = getBookingServiceHelper()
                    .fetchBookedDetails(roomList, fromDate, toDate, bookingDetailsRepository);

            response = getBookingServiceHelper().frameResponse(type, fromDate, toDate,
                    listOfBookingDetailsForTheRoomType);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = ResponseBuilder.buildFailureResponse(e.getMessage());
        }
        logger.debug("Response : {} ", response);
        return response;
    }

    public RoomValidator getRoomValidator() {
        return new RoomValidator();
    }

    public BookingServiceHelper getBookingServiceHelper() {
        return new BookingServiceHelper();
    }
}