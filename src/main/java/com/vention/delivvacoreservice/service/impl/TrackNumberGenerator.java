package com.vention.delivvacoreservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class TrackNumberGenerator {

    public String generateTrackNumber(String cityFrom, String cityTo) {
        return cityFrom.substring(0,3) + generateDigits() + cityTo.substring(0,3);
    }

    private int generateDigits(){
        Random random = new Random();
        return 1000000 + random.nextInt(9000000);
    }
}
