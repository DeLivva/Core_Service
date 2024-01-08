package com.vention.delivvacoreservice.fake_geo_data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vention.delivvacoreservice.dto.request.OrderDestinationsDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FakeGeoPositionSenderService {
    private static final List<String> processingTracks = new ArrayList<>();
    private static final String queryString =
            "SELECT NEW com.vention.delivvacoreservice.dto.request.OrderDestinationsDTO " +
            "(o.courierId, o.trackNumber, odStart.latitude, odStart.longitude, odFinal.latitude, odFinal.longitude) " +
            "FROM orders o JOIN o.startingDestination odStart JOIN o.finalDestination odFinal " +
            "WHERE o.status = 'PICKED_UP'";
    private final EntityManager entityManager;
    private final FakeGeoPositionHelperService helperService;

    @Scheduled(fixedRate = 120000)
    public void start() throws JsonProcessingException, InterruptedException {
        TypedQuery<OrderDestinationsDTO> query = entityManager.createQuery(queryString, OrderDestinationsDTO.class);
        for (OrderDestinationsDTO dto : query.getResultList()) {
            String track = dto.getTrackNumber();
            if (!processingTracks.contains(track)) {
                log.info("Sending points for track: " + track);
                processingTracks.add(track);
                helperService.getPointsAndSend(dto);
            }
        }
    }
}
