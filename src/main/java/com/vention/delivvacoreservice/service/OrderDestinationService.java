package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.dto.GeolocationDTO;

import java.util.List;

public interface OrderDestinationService {

    OrderDestination getOrderDestinationWithValidation(GeolocationDTO geolocation);

    boolean areDestinationsValid(List<GeolocationDTO> destinations);
}
