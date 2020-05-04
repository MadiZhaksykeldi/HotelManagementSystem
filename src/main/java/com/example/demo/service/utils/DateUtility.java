package com.example.demo.service.utils;

import com.example.demo.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateUtility {
    private DateUtility() {

    }

    public static Date getDateFromString(String date) throws InvalidDateException {
        LocalDate localDate = getLocalDateFromString(date);
        return convertToUtilDate(localDate);
    }

    public static void validateDate(Date fromDate, Date toDate) throws InvalidDateException {

        if (fromDate.after(toDate))
            throw new InvalidDateException("From date should not be greater than to date");

    }

    public static void validateDate(String fromDate, String toDate) throws InvalidDateException {
        Date fD = convertToUtilDate(getLocalDateFromString(fromDate));
        Date tD = convertToUtilDate(getLocalDateFromString(toDate));
        validateDate(fD, tD);
    }

    private static Date convertToUtilDate(LocalDate dateToConvert) throws InvalidDateException {
        try {
            return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new InvalidDateException("Invalid date format");
        }
    }


    public static Integer findNumberOfDays(String fromDate, String toDate) throws InvalidDateException {
        try {
            LocalDate fromDt = getLocalDateFromString(fromDate);
            LocalDate toDt = getLocalDateFromString(toDate);
            Period period = getPeriod(fromDt, toDt);
            return period.getDays() + 1;
        } catch (Exception e) {
            throw new InvalidDateException();
        }
    }

    private static Period getPeriod(LocalDate fromDate, LocalDate toDate) {
        return Period.between(fromDate, toDate);
    }

    public static LocalDate getLocalDateFromString(String date) {
        return LocalDate.parse(date);
    }

    public static List<LocalDate> getInBetweenDates(String fromDateInString, String toDateInString) {
        LocalDate startDate = getLocalDateFromString(fromDateInString);
        LocalDate endDate = getLocalDateFromString(toDateInString);
        return new ArrayList<>(getDates(startDate, endDate));
    }

    private static List<LocalDate> getDates(LocalDate begin, LocalDate end) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(begin, end) + 1;
        return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(i -> begin.plusDays(i))
                .collect(Collectors.toList());
    }

    public static boolean checkInRange(String startDateInString, String endDateInString,
                                       String existingStartDateInString, String existingEndDateInString) {
        LocalDate startDate = getLocalDateFromString(startDateInString);
        LocalDate endDate = getLocalDateFromString(endDateInString);
        LocalDate existingStartDate = getLocalDateFromString(existingStartDateInString);
        LocalDate existingEndDate = getLocalDateFromString(existingEndDateInString);
        LocalDate maxdate = max(startDate, endDate);
        LocalDate minDate = min(startDate, endDate);
        LocalDate existingMinDate = min(existingStartDate, existingEndDate);
        LocalDate existingMaxDate = max(existingStartDate, existingEndDate);
        List<LocalDate> existingIntervalDates = getDates(existingStartDate, existingEndDate);
        List<LocalDate> intervalDates = getDates(startDate, endDate);
        return (existingIntervalDates.contains(maxdate) || existingIntervalDates.contains(minDate)
                || intervalDates.contains(existingMinDate) || intervalDates.contains(existingMaxDate));
    }

    public static LocalDate max(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null)
            return null;
        if (date1 == null)
            return date2;
        if (date2 == null)
            return date1;
        return (date1.isAfter(date2)) ? date1 : date2;
    }

    public static LocalDate min(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null)
            return null;
        if (date1 == null)
            return date2;
        if (date2 == null)
            return date1;
        return (date1.isBefore(date2)) ? date1 : date2;
    }
}