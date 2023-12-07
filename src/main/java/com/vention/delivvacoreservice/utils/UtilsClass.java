package com.vention.delivvacoreservice.utils;

import com.vention.delivvacoreservice.dto.GeolocationDTO;
import com.vention.delivvacoreservice.exception.BadRequestException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UtilsClass {

    public static Timestamp convertStringToTimestamp(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(time);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new BadRequestException("Invalid date time format....!!");
        }
    }

    public static boolean validateDestinations(List<GeolocationDTO> destinations) {
        for (GeolocationDTO destination : destinations) {
            if(destination.getLatitude() == null || destination.getLongitude() == null) {
                return false;
            }
        }
        return true;
    }
}
