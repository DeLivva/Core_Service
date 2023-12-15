package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.feign_clients.MapClient;
import com.vention.delivvacoreservice.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrackNumberGenerator {

    private final MapUtils mapUtils;

    public String generateTrackNumber(OrderDestination from, OrderDestination to) {
        String startPlace = mapUtils.getCityNameByCoordinates(from);
        String finalPlace = mapUtils.getCityNameByCoordinates(to);
        return startPlace.substring(0,3) + UUID.randomUUID() + finalPlace.substring(0,3);
    }
}
