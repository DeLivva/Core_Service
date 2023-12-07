package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.dto.GeolocationDTO;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderDestinationRepository;
import com.vention.delivvacoreservice.service.OrderDestinationService;
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
        OrderDestination orderDestination;
        Optional<OrderDestination> optionalDestination = orderDestinationRepository.findByLongitudeAndLatitude(
                geolocation.getLongitude(),
                geolocation.getLatitude()
        );
        orderDestination = optionalDestination.orElseGet(
                () -> orderDestinationRepository.save(orderMapper.mapOrderRequestToOrderDestination(geolocation)
        ));
        return orderDestination;
    }

    @Override
    public boolean areDestinationsValid(List<GeolocationDTO> destinations) {
        for (GeolocationDTO destination : destinations) {
            if(destination.getLatitude() == null || destination.getLongitude() == null) {
                return false;
            }
        }
        return true;
    }
}
