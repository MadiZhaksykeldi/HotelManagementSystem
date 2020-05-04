package com.example.demo.service.utils;

import com.example.demo.constants.Constants;

import java.util.Collection;

public class CommonUtils {
    private CommonUtils() {

    }

    public static boolean isEmptyOrNullObject(Object value) {
        return null == value || Constants.EMPTY.equals(value);
    }

    public static boolean isEmptyOrNullCollection(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }
}