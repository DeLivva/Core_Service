package com.vention.delivvacoreservice.utils;

import com.vention.delivvacoreservice.exception.BadRequestException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Timestamp convertStringToTimestamp(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(time);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new BadRequestException("Invalid date time format....!!");
        }
    }
}
