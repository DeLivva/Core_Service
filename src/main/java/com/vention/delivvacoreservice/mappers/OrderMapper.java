package com.vention.delivvacoreservice.mappers;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.dto.mail.OrderMailDTO;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.general.lib.dto.response.GeolocationDTO;
import com.vention.general.lib.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface OrderMapper {

    @Mapping(source = "userId", target = "customerId")
    Order mapOrderRequestToEntity(OrderCreationRequestDTO request);

    OrderDestination mapOrderRequestToOrderDestination(GeolocationDTO request);

    GeolocationDTO mapOrderDestinationToResponse(OrderDestination destination);

    OrderResponseDTO mapOrderEntityToResponse(Order order);

    OrderMailDTO mapOrderEntityToOrderMailDTO(Order order);

    default Map<String, Object> orderMailDTOToMap(OrderMailDTO orderMailDTO) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", orderMailDTO.getId());
        map.put("startLocation", orderMailDTO.getStartLocation());
        map.put("finalLocation", orderMailDTO.getFinalLocation());
        map.put("deliveryDate", orderMailDTO.getDeliveryDate());
        map.put("description", orderMailDTO.getItemDescription());
        map.put("trackNumber", orderMailDTO.getTrackNumber());
        map.put("sender", orderMailDTO.getSender());
        return map;
    }
}
