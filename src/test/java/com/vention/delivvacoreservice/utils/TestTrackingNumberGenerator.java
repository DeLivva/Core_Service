package com.vention.delivvacoreservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestTrackingNumberGenerator {
    private static TrackingNumberGenerator trackingNumberGenerator;

    @BeforeAll
    static void beforeAll() {
        trackingNumberGenerator = new DefaultTrackingNumberGenerator();
    }

    @Test
    public void testGenerateTrackingNumber() {
        String trackingNumber = trackingNumberGenerator.generateTrackingNumber("Tashkent", "Samarkand");
        String regex = "[A-Z]{3}\\d{14}[A-Z]{3}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(trackingNumber);

        Assertions.assertTrue(matcher.matches());
    }
}
