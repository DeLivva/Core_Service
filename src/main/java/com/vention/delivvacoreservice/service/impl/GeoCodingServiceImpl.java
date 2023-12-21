package com.vention.delivvacoreservice.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vention.delivvacoreservice.service.GeoCodingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeoCodingServiceImpl implements GeoCodingService {
    @Value("${maps.api.key}")
    private String apiKey;
    @Value("${maps.api.url}")
    private String apiUrl;
    private static final Logger log = LoggerFactory.getLogger(GeoCodingServiceImpl.class);

    @Override
    public String getCityName(double latitude, double longitude) {
        try {
            String encodedLocation = URLEncoder.encode(latitude + "," + longitude, StandardCharsets.UTF_8);
            URL url = new URL(apiUrl + encodedLocation + "&key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String jsonString = response.toString();
                // Parse JSON response to get city name
                return parseCityFromGoogleJson(jsonString);
            } else {
                log.error("HTTP error code: " + responseCode);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private String parseCityFromGoogleJson(String jsonString) {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonArray results = jsonObject.getAsJsonArray("results");

        if (!results.isEmpty()) {
            JsonObject addressComponent = results.get(0).getAsJsonObject();
            JsonArray addressComponents = addressComponent.getAsJsonArray("address_components");

            for (int i = 0; i < addressComponents.size(); i++) {
                JsonObject component = addressComponents.get(i).getAsJsonObject();
                JsonArray types = component.getAsJsonArray("types");

                // Check for different types that might indicate the city
                for (int j = 0; j < types.size(); j++) {
                    JsonElement type = types.get(j);
                    if (StringUtils.equalsAny(type.getAsString(), "locality", "postal_town", "administrative_area_level_1")) {
                        return component.getAsJsonPrimitive("long_name").getAsString();
                    }

                }
            }

        }
        return null;
    }
}


