package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;
import com.vention.general.lib.enums.OrderStatus;

public interface OrderService {

    OrderResponseDTO createOrder(OrderCreationRequestDTO request);

    OrderStatus getStatus(Long id);

    void setStatus(Long id, OrderStatus status);
}
