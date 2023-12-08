package com.vention.delivvacoreservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.exception.JsonParsingException;
import com.vention.delivvacoreservice.feign_clients.MapClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrackNumberGenerator {

    private final MapClient mapClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateTrackNumber(OrderDestination from, OrderDestination to) {
        String startPlace = getDestinationName(from);
        String finalPlace = getDestinationName(to);
        return startPlace.substring(0,3) + UUID.randomUUID() + finalPlace.substring(0,3);
    }

    public String getDestinationName(OrderDestination destination) {
        double longitude = destination.getLongitude();
        double latitude = destination.getLatitude();
        String jsonData = mapClient.getLocationByCoordinates(latitude, longitude, "json");
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            return jsonNode.path("address").path("city").asText();
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e.getMessage());
        }
    }
}
