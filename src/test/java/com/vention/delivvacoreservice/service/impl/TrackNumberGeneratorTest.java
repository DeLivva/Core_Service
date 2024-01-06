package com.vention.delivvacoreservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
class TrackNumberGeneratorTest {
    @InjectMocks
    private TrackNumberGenerator trackNumberGenerator;

    @Test
    void testGenerateTrackNumber2() {
        // given
        String cityFrom = "PARIS";
        String cityTo = "LONDON";
        String trackNumber = trackNumberGenerator.generateTrackNumber(cityFrom, cityTo);

        //then
        assertTrue(trackNumber.matches("[A-Z]{3}\\d{7}[A-Z]{3}"));
        assertTrue(trackNumber.startsWith(cityFrom.substring(0, 3)));
        assertTrue(trackNumber.endsWith(cityTo.substring(0, 3)));
    }
}