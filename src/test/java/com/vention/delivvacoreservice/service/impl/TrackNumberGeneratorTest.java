package com.vention.delivvacoreservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.exception.JsonParsingException;
import com.vention.delivvacoreservice.feign_clients.MapClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrackNumberGeneratorTest {

    @Mock
    private MapClient mapClient;

    @InjectMocks
    private TrackNumberGenerator trackNumberGenerator;

    private OrderDestination destination;

    @BeforeEach
    void setUp() {
        destination = new OrderDestination(12345D, 2345D);
    }

    @Test
    void testGenerateTrackNumber() throws JsonProcessingException {
        // given
        String jsonData = "{\"address\": {\"city\":\"Tashkent\"}}";
        OrderDestination finalPlace = new OrderDestination(23453D, 45343D);
        // when
        doReturn(jsonData).when(mapClient).getLocationByCoordinates(anyDouble(), anyDouble(), eq("json"));
        String trackNumber = trackNumberGenerator.generateTrackNumber(destination, finalPlace);
        // then
        assertEquals(trackNumber.substring(0, 3), "Tas");
        verify(mapClient, times(2)).getLocationByCoordinates(anyDouble(), anyDouble(), anyString());
    }

    @Test
    void testGetDestinationName() {
        // given
        String jsonData = "{\"address\": {\"city\":\"Tashkent\"}}";
        // when
        doReturn(jsonData).when(mapClient).getLocationByCoordinates(anyDouble(), anyDouble(), anyString());
        // then
        assertDoesNotThrow(() -> trackNumberGenerator.getDestinationName(destination));
        verify(mapClient, times(1)).getLocationByCoordinates(anyDouble(), anyDouble(), anyString());
    }

    @Test
    void WillGetDestinationNameTestThrow() {
        // given
        String jsonData = "will throw";
        // when
        doReturn(jsonData).when(mapClient).getLocationByCoordinates(anyDouble(), anyDouble(), anyString());
        // then
        assertThrows(JsonParsingException.class, () -> trackNumberGenerator.getDestinationName(destination));
        verify(mapClient, times(1)).getLocationByCoordinates(anyDouble(), anyDouble(), anyString());
    }
}