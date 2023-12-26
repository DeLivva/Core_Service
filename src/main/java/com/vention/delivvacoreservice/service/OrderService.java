package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.general.lib.dto.response.OrderResponseDTO;
import com.vention.general.lib.enums.OrderStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderCreationRequestDTO request);

    OrderResponseDTO findById(Long id);

    void setStatus(Long id, OrderStatus status);

    void offerTheDelivery(boolean byCustomer, Long courierId, Long orderId);

    void approveAnOffer(Long courierId, Long orderId);

    void rejectAnOrder(Long userId, Long orderId);

    Order getById(Long orderId);

    List<OrderResponseDTO> getOrderList();

    ResponseEntity<List<OrderResponseDTO>> getByFilter(int page, int size, String startPoint, String endPoint, Date date);

    Order getOrderByCustomerId(Long customerId, Long orderId);
}
