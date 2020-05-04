package com.example.demo.service.helper;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.exception.InvalidDateException;
import com.example.demo.model.PriceDetail;
import com.example.demo.model.Room;
import com.example.demo.service.utils.CommonUtils;
import com.example.demo.service.utils.DateUtility;
import com.example.demo.repository.PriceDetailRepository;

public class PriceCalculator {
    Logger logger = LoggerFactory.getLogger(PriceCalculator.class);
    private static AtomicLong priceDetailsId = new AtomicLong();

    public PriceDetail calculate(Room room, String fromDateInString, String toDateInString,
                                 PriceDetailRepository priceDetailRepository) throws InvalidDateException {
        Date fromDate = DateUtility.getDateFromString(fromDateInString);
        Date toDate = DateUtility.getDateFromString(toDateInString);
        int noOfDays = DateUtility.findNumberOfDays(fromDateInString, toDateInString);
        int totalPrice;
        PriceDetail priceDetail = priceDetailRepository.findByRoomIdAndDateRange(room.getId(), fromDate, toDate);

        if (CommonUtils.isEmptyOrNullObject(priceDetail)) {
            logger.debug("DB information is null for the {} so calculating the price manually", room.getId());
            totalPrice = noOfDays == 0 ? room.getPrice() : noOfDays * room.getPrice();
        } else {
            logger.debug("Price for {} in DB is {}", room.getId(), priceDetail.getPrice());
            int pricePerDay = priceDetail.getPrice();
            totalPrice = noOfDays == 0 ? pricePerDay : noOfDays * pricePerDay;
        }
        priceDetail = buildPriceDetails(room, fromDate, toDate, totalPrice);
        logger.debug("PriceDetail : {}", priceDetail);
        return priceDetail;
    }

    private PriceDetail buildPriceDetails(Room room, Date fromDate, Date toDate, int totalPrice) {
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.setId(priceDetailsId.getAndIncrement());
        priceDetail.setRoomType(room.getType());
        priceDetail.setPrice(totalPrice);
        priceDetail.setFromDate(fromDate);
        priceDetail.setToDate(toDate);
        priceDetail.setRoomId(room.getId());
        return priceDetail;
    }
}