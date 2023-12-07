package com.vention.delivvacoreservice.mappers;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.domain.OrderEntity;
import com.vention.delivvacoreservice.dto.GeolocationDTO;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {

    @Mapping(source = "userId", target = "customerId")
    OrderEntity mapOrderRequestToEntity(OrderCreationRequestDTO request);

    OrderDestination mapOrderRequestToOrderDestination(GeolocationDTO request);

    GeolocationDTO mapOrderDestinationToResponse(OrderDestination destination);

    OrderResponseDTO mapOrderEntityToResponse(OrderEntity order);
}
