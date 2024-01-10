package com.vention.delivvacoreservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.exception.JsonParsingException;
import com.vention.delivvacoreservice.feign_clients.MapClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapUtils {

    private final MapClient mapClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getCityNameByCoordinates(OrderDestination destination) {
        double longitude = destination.getLongitude();
        double latitude = destination.getLatitude();
        String jsonData = mapClient.getLocationByCoordinates(latitude, longitude, "json", "en");
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            return jsonNode.path("display_name").asText();
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e.getMessage());
        }
    }
}
