package com.vention.delivvacoreservice.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DefaultTrackingNumberGenerator implements TrackingNumberGenerator {

    @Override
    public String generateTrackingNumber(String cityFrom, String cityTo) {
        String formattedDateTime = LocalDateTime
                .now(ZoneId.of("Asia/Tashkent"))
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return getCityCode(cityFrom) + formattedDateTime + getCityCode(cityTo);
    }

    private String getCityCode(String city) {
        return city.toUpperCase().substring(0, 3);
    }
}
