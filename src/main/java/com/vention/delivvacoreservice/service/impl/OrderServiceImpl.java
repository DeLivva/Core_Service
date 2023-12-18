package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.dto.mail.OrderMailDTO;
import com.vention.delivvacoreservice.dto.mail.Sender;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.feign_clients.UserClient;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderRepository;
import com.vention.delivvacoreservice.service.MailService;
import com.vention.delivvacoreservice.service.OrderDestinationService;
import com.vention.delivvacoreservice.service.OrderService;
import com.vention.delivvacoreservice.utils.MapUtils;
import com.vention.general.lib.dto.response.GeolocationDTO;
import com.vention.general.lib.dto.response.OrderResponseDTO;
import com.vention.general.lib.dto.response.UserResponseDTO;
import com.vention.general.lib.enums.OrderStatus;
import com.vention.general.lib.exceptions.BadRequestException;
import com.vention.general.lib.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.vention.general.lib.utils.DateUtils.convertStringToTimestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserClient userClient;
    private final OrderMapper orderMapper;
    private final MapUtils mapUtils;
    private final MailService mailService;
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
        return convertEntityToResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO findById(Long id) {
        var order = getById(id);
        return convertEntityToResponseDTO(order);
    }

    @Override
    public void setStatus(Long id, OrderStatus status) {
        var order = getById(id);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public void offerTheDelivery(boolean byCustomer, Long courierId, Long orderId) {
        Order order = getById(orderId);
        UserResponseDTO courier = userClient.getUserById(courierId);
        UserResponseDTO customer = userClient.getUserById(order.getCustomerId());
        OrderMailDTO mailDTO = orderMapper.mapOrderEntityToOrderMailDTO(order);
        mailDTO.setCourier(courier);
        mailDTO.setCustomer(customer);
        mailDTO.setStartLocation(mapUtils.getCityNameByCoordinates(order.getStartingDestination()));
        mailDTO.setFinalLocation(mapUtils.getCityNameByCoordinates(order.getFinalDestination()));
        mailDTO.setSender((byCustomer) ? Sender.CUSTOMER : Sender.COURIER);
        mailService.sendAnOffer(mailDTO);
    }

    @Override
    public void approveAnOffer(Long courierId, Long orderId) {
        Order order = getById(orderId);
        if (order.getCourierId() != null) {
            throw new BadRequestException("Order has already been assigned to the courier");
        }
        UserResponseDTO courier = userClient.getUserById(courierId);
        order.setCourierId(courier.getId());
        order.setStatus(OrderStatus.PICKED_UP);
        orderRepository.save(order);
    }

    @Override
    public void rejectAnOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (Objects.equals(order.getCustomerId(), userId)) {
            if (order.getStatus() == OrderStatus.CREATED || order.getStatus() == OrderStatus.PICKED_UP) {
                order.setStatus(OrderStatus.REJECTED_BY_CUSTOMER);
            } else {
                throw new BadRequestException("Order cannot be canceled!!!");
            }
        } else if (Objects.equals(order.getCourierId(), userId)) {
            if (order.getStatus() == OrderStatus.PICKED_UP) {
                order.setStatus(OrderStatus.REJECTED_BY_COURIER);
            } else {
                throw new BadRequestException("Order cannot be canceled!!!");
            }
        }
        orderRepository.save(order);
    }

    @Override
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Order not found on id : " + orderId)
        );
    }

    @Override
    public List<OrderResponseDTO> getOrderList() {
        return orderRepository.findAllByStatus()
                .stream()
                .map(orderMapper::mapOrderEntityToResponse)
                .toList();
    }

    private OrderResponseDTO convertEntityToResponseDTO(Order order) {
        var orderResponseDTO = orderMapper.mapOrderEntityToResponse(order);
//        orderResponseDTO.setCostumer(userClient.getUserById(order.getCustomerId()));
//        orderResponseDTO.setCourier(userClient.getUserById(order.getCourierId()));
        return orderResponseDTO;
    }
}
