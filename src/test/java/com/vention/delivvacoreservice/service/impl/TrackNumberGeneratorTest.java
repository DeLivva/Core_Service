package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.utils.MapUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrackNumberGeneratorTest {

    @Mock
    private MapUtils mapUtils;

    @InjectMocks
    private TrackNumberGenerator trackNumberGenerator;

    private OrderDestination destination;

    @BeforeEach
    void setUp() {
        destination = new OrderDestination(12345D, 2345D, "city");
    }

    @Test
    void testGenerateTrackNumber() {
        // given
        OrderDestination finalPlace = new OrderDestination(23453D, 45343D, "city");
        String trackNumber = trackNumberGenerator.generateTrackNumber(destination, finalPlace);
        // then
        Assertions.assertNotNull(trackNumber);
        verify(mapUtils, times(2)).getPostalCodesByCoordinates(any());
    }
}