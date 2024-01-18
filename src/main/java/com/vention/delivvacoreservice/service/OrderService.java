package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.dto.request.OrderFilterDto;
import com.vention.delivvacoreservice.dto.request.OrderParticipantsDto;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.request.TrackNumberResponseDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseWithDistance;
import com.vention.general.lib.dto.response.OrderResponseDTO;
import com.vention.general.lib.enums.OrderStatus;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderResponseDTO createOrder(OrderCreationRequestDTO request);

    OrderResponseDTO findById(Long id);

    OrderResponseWithDistance getByIdWithAddress(Long id);

    void setStatus(Long id, OrderStatus status);

    void offerTheDelivery(boolean byCustomer, Long courierId, Long orderId);

    void approveAnOffer(Long courierId, Long orderId);

    void rejectAnOrder(Long userId, Long orderId);

    Order getById(Long orderId);

    List<OrderResponseDTO> getByFilter(int page, int size, OrderFilterDto filterDto);

    Order getOrderByCustomerId(Long customerId, Long orderId);

    List<OrderResponseDTO> getActiveOrders(OrderParticipantsDto dto);

    List<OrderResponseDTO> getHistoryOrders(OrderParticipantsDto dto);

    void finishOrderByCourier(Long orderId);

    TrackNumberResponseDTO getTrackNumberByOrderId(Long orderId);
}
