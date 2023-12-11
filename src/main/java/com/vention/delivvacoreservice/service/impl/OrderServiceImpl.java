package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.dto.GeolocationDTO;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;
import com.vention.delivvacoreservice.dto.response.UserResponseDTO;
import com.vention.delivvacoreservice.feign_clients.UserClient;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderRepository;
import com.vention.delivvacoreservice.service.OrderDestinationService;
import com.vention.delivvacoreservice.service.OrderService;
import com.vention.general.lib.enums.OrderStatus;
import com.vention.general.lib.exceptions.BadRequestException;
import com.vention.general.lib.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.vention.general.lib.utils.DateUtils.convertStringToTimestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserClient userClient;
    private final OrderMapper orderMapper;
    private final TrackNumberGenerator trackNumberGenerator;
    private final OrderDestinationService orderDestinationService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderCreationRequestDTO request) {
        GeolocationDTO startingDestinationDTO = request.getStartingDestination();
        GeolocationDTO finalDestinationDTO = request.getFinalDestination();
        if (!orderDestinationService.areDestinationsValid(List.of(startingDestinationDTO, finalDestinationDTO))) {
            throw new BadRequestException("Invalid location data is provided");
        }
        UserResponseDTO customer = userClient.getUserById(request.getUserId());
        OrderDestination savedStartingPlace = orderDestinationService
                .getOrderDestinationWithValidation(startingDestinationDTO);
        OrderDestination savedFinalPlace = orderDestinationService
                .getOrderDestinationWithValidation(finalDestinationDTO);
        Order order = orderMapper.mapOrderRequestToEntity(request);
        order.setDeliveryDate(convertStringToTimestamp(request.getScheduledDeliveryDate()));
        order.setTrackNumber(trackNumberGenerator.generateTrackNumber(savedStartingPlace, savedFinalPlace));
        order.setStartingDestination(savedStartingPlace);
        order.setFinalDestination(savedFinalPlace);
        Order savedOrder = orderRepository.save(order);
        OrderResponseDTO orderResponse = orderMapper.mapOrderEntityToResponse(savedOrder);
        orderResponse.setCostumer(customer);
        return orderResponse;
    }

    @Override
    public OrderStatus getStatus(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("order not found on id" + id));
        return order.getStatus();
    }

    @Override
    public void setStatus(Long id, OrderStatus status) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("order not found on id" + id));
        order.setStatus(status);
    }
}
