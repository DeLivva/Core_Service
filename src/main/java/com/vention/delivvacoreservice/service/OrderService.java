package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;
import com.vention.general.lib.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderCreationRequestDTO request);

    OrderStatus getStatus(Long id);

    void setStatus(Long id, OrderStatus status);

    void offerTheDelivery(boolean byCustomer, Long courierId, Long orderId);

    void approveAnOffer(Long courierId, Long orderId);

    void rejectAnOrder(Long userId, Long orderId);

    Order getById(Long orderId);

    List<OrderResponseDTO> getOrderList();
}
