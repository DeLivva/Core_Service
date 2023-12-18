package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.utils.MapUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
        destination = new OrderDestination(12345D, 2345D);
    }

    @Test
    void testGenerateTrackNumber() {
        // given
        OrderDestination finalPlace = new OrderDestination(23453D, 45343D);
        // when
        doReturn("Tashkent").when(mapUtils).getCityNameByCoordinates(any());
        String trackNumber = trackNumberGenerator.generateTrackNumber(destination, finalPlace);
        // then
        assertEquals(trackNumber.substring(0, 3), "Tas");
        verify(mapUtils, times(2)).getCityNameByCoordinates(any());
    }
}