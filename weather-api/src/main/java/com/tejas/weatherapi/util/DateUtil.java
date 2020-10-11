package com.tejas.weatherapi.util;

import org.springframework.context.annotation.Bean;

import java.util.Date;

public class DateUtil {
    public static Date toDate(long epoch){
        return new Date(epoch * 1000);
    }

    public static Long toEpoch(Date date) {
        return date.getTime();
    }
}
