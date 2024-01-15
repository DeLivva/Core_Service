package com.vention.delivvacoreservice.fake_geo_data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.delivvacoreservice.dto.request.OrderDestinationsDTO;
import com.vention.delivvacoreservice.service.rabbitmq.producer.RabbitMQProducer;
import com.vention.general.lib.dto.GeoPositionInfoDTO;
import com.vention.general.lib.dto.response.GeolocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FakeGeoPositionHelperService {
    private static final String URL = "https://graphhopper.com/api/1/route";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final RabbitMQProducer producer;

    @Async
    public void getPointsAndSend(OrderDestinationsDTO result) throws JsonProcessingException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder(URL);
        stringBuilder.append("?point=");
        stringBuilder.append(result.getStartLatitude());
        stringBuilder.append(",");
        stringBuilder.append(result.getStartLongitude());
        stringBuilder.append("&");
        stringBuilder.append("point=");
        stringBuilder.append(result.getFinalLatitude());
        stringBuilder.append(",");
        stringBuilder.append(result.getFinalLongitude());
        stringBuilder.append("&profile=car&locale=en&calc_points=true&key=bc37f86d-e020-4df0-ab84-52138216bd84&points_encoded=false");
        try {
            JsonNode points = Objects.requireNonNull(restTemplate.getForObject(stringBuilder.toString(), JsonNode.class))
                    .get("paths").get(0).get("points").get("coordinates");

            List<GeolocationDTO> myObjectList = new ArrayList<>();

            for (JsonNode element : points) {
                List<Double> list = objectMapper.treeToValue(element, List.class);
                GeolocationDTO myObject = GeolocationDTO.builder()
                        .longitude(list.get(0))
                        .latitude(list.get(1))
                        .build();
                myObjectList.add(myObject);
            }

            for (GeolocationDTO obj : myObjectList) {
                producer.sendGeoPosition(GeoPositionInfoDTO.builder()
                        .userId(result.getUserId())
                        .latitudeAndLongitude(obj)
                        .trackNumber(result.getTrackNumber())
                        .build());
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            return;
        }
    }
}
