package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;

public interface OrderService {

    OrderResponseDTO createOrder(OrderCreationRequestDTO request);

}
