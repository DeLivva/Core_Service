package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderDestinationRepository;
import com.vention.delivvacoreservice.service.OrderDestinationService;
import com.vention.general.lib.dto.response.GeolocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDestinationServiceImpl implements OrderDestinationService {

    private final OrderMapper orderMapper;
    private final OrderDestinationRepository orderDestinationRepository;

    @Override
    public OrderDestination getOrderDestinationWithValidation(GeolocationDTO geolocation) {
        Optional<OrderDestination> optionalDestination = orderDestinationRepository.findByLongitudeAndLatitude(
                geolocation.getLongitude(),
                geolocation.getLatitude()
        );
        return optionalDestination.orElseGet(
                () -> orderDestinationRepository.save(orderMapper.mapOrderRequestToOrderDestination(geolocation)
        ));
    }

    @Override
    public boolean areDestinationsValid(List<GeolocationDTO> destinations) {
        return destinations.stream().allMatch(
                destination -> destination.getLongitude() != null && destination.getLatitude() != null
        );
    }
}
