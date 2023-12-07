package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.domain.OrderEntity;
import com.vention.delivvacoreservice.dto.response.UserResponseDTO;
import com.vention.delivvacoreservice.exception.BadRequestException;
import com.vention.delivvacoreservice.feign_clients.UserClient;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderDestinationRepository;
import com.vention.delivvacoreservice.repository.OrderRepository;
import com.vention.delivvacoreservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.vention.delivvacoreservice.utils.UtilsClass.convertStringToTimestamp;
import static com.vention.delivvacoreservice.utils.UtilsClass.validateDestinations;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserClient userClient;
    private final OrderMapper orderMapper;
    private final TrackNumberGenerator trackNumberGenerator;
    private final OrderRepository orderRepository;
    private final OrderDestinationRepository orderDestinationRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderCreationRequestDTO request) {
        if(!validateDestinations(List.of(request.getStartingDestination(), request.getFinalDestination()))) {
            throw new BadRequestException("Invalid location data is provided");
        }
        UserResponseDTO customer = userClient.getUserById(request.getUserId());
        OrderDestination savedStartingPlace = orderDestinationRepository.save(
                orderMapper.mapOrderRequestToOrderDestination(request.getStartingDestination())
        );
        OrderDestination savedFinalPlace = orderDestinationRepository.save(
                orderMapper.mapOrderRequestToOrderDestination(request.getStartingDestination())
        );
        OrderEntity order = orderMapper.mapOrderRequestToEntity(request);
        order.setDeliveryDate(convertStringToTimestamp(request.getScheduledDeliveryDate()));
        order.setTrackNumber(trackNumberGenerator.generateTrackNumber(savedStartingPlace, savedFinalPlace));
        order.setStartingDestination(savedStartingPlace);
        order.setFinalDestination(savedFinalPlace);
        OrderEntity savedOrder = orderRepository.save(order);
        OrderResponseDTO orderResponse = orderMapper.mapOrderEntityToResponse(savedOrder);
        orderResponse.setCostumer(customer);
        return orderResponse;
    }
}
