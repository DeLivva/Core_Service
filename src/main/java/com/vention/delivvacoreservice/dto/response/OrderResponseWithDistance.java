package com.vention.delivvacoreservice.dto.response;

import com.vention.general.lib.dto.response.OrderResponseDTO;
import lombok.Data;

@Data
public class OrderResponseWithDistance extends OrderResponseDTO {

    private double distance;

    public OrderResponseWithDistance(OrderResponseDTO order, double distance) {
        super(order.getId(), order.getItemDescription(), order.getStartingDestination(), order.getFinalDestination(), order.getStartingPlace(), order.getFinalPlace(),
                order.getCostumer(), order.getCourier(), order.getDeliveryDate(), order.getStatus(), order.getDeliveryStartedAt(), order.getDeliveryFinishedAt());
        this.distance = distance;
    }
}
