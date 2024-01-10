package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.domain.OrderInvitation;
import com.vention.delivvacoreservice.dto.request.OrderFilterDto;
import com.vention.delivvacoreservice.dto.request.OrderParticipantsDto;
import com.vention.delivvacoreservice.dto.mail.OrderMailDTO;
import com.vention.delivvacoreservice.dto.mail.Sender;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.enums.InvitationStatus;
import com.vention.delivvacoreservice.repository.OrderInvitationsRepository;
import com.vention.delivvacoreservice.service.GeoCodingService;
import com.vention.general.lib.dto.response.UserResponseDTO;
import com.vention.delivvacoreservice.feign_clients.AuthServiceClient;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderRepository;
import com.vention.delivvacoreservice.service.MailService;
import com.vention.delivvacoreservice.service.OrderDestinationService;
import com.vention.delivvacoreservice.service.OrderService;
import com.vention.delivvacoreservice.utils.MapUtils;
import com.vention.general.lib.dto.response.GeolocationDTO;
import com.vention.general.lib.dto.response.OrderResponseDTO;
import com.vention.general.lib.enums.OrderStatus;
import com.vention.general.lib.exceptions.BadRequestException;
import com.vention.general.lib.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.vention.general.lib.utils.DateUtils.convertStringToTimestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AuthServiceClient authServiceClient;
    private final OrderMapper orderMapper;
    private final MapUtils mapUtils;
    private final MailService mailService;
    private final TrackNumberGenerator trackNumberGenerator;
    private final OrderDestinationService orderDestinationService;
    private final OrderRepository orderRepository;
    private final GeoCodingService geoCodingService;
    private final OrderInvitationsRepository orderInvitationsRepository;

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
        String startingCity = geoCodingService.getCityName(request.getStartingDestination().getLatitude(), request.getStartingDestination().getLongitude());
        savedStartingPlace.setCity(startingCity);
        order.setStartingDestination(savedStartingPlace);
        String finalCity = geoCodingService.getCityName(request.getFinalDestination().getLatitude(), request.getFinalDestination().getLongitude());
        savedFinalPlace.setCity(finalCity);
        order.setFinalDestination(savedFinalPlace);
        order.setTrackNumber(trackNumberGenerator.generateTrackNumber(startingCity, finalCity));
        Order savedOrder = orderRepository.save(order);
        return convertEntityToResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO findById(Long id) {
        var order = getById(id);
        OrderResponseDTO orderResponseDTO = convertEntityToResponseDTO(order);
        orderResponseDTO.setStartingPlace(mapUtils.getCityNameByCoordinates(order.getStartingDestination()));
        orderResponseDTO.setFinalPlace(mapUtils.getCityNameByCoordinates(order.getFinalDestination()));
        return orderResponseDTO;
    }

    @Override
    public void setStatus(Long id, OrderStatus status) {
        var order = getById(id);
        order.setStatus(status);
        orderRepository.save(order);

        mailService.sendStatusUpdateNotification(order);
    }

    @Override
    @Transactional
    public void offerTheDelivery(boolean byCustomer, Long courierId, Long orderId) {
        Order order = getById(orderId);
        UserResponseDTO courier = authServiceClient.getUserById(courierId);
        UserResponseDTO customer = authServiceClient.getUserById(order.getCustomerId());

        OrderInvitation orderInvitation;
        if (byCustomer) {
            orderInvitation = new OrderInvitation(customer.getId(), courierId, orderId, InvitationStatus.PENDING);
        } else {
            orderInvitation = new OrderInvitation(courierId, customer.getId(), orderId, InvitationStatus.PENDING);
        }
        orderInvitationsRepository.save(orderInvitation);

        OrderMailDTO mailDTO = orderMapper.mapOrderEntityToOrderMailDTO(order);
        mailDTO.setCourier(courier);
        mailDTO.setCustomer(customer);
        mailDTO.setStartLocation(mapUtils.getCityNameByCoordinates(order.getStartingDestination()));
        mailDTO.setFinalLocation(mapUtils.getCityNameByCoordinates(order.getFinalDestination()));
        mailDTO.setSender((byCustomer) ? Sender.CUSTOMER : Sender.COURIER);
        mailDTO.setTrackNumber(order.getTrackNumber());
        mailService.sendAnOffer(mailDTO);
    }

    @Override
    @Transactional
    public void approveAnOffer(Long courierId, Long orderId) {
        Order order = getById(orderId);
        if (order.getCourierId() != null) {
            throw new BadRequestException("Order has already been assigned to the courier");
        }
        orderInvitationsRepository.approveOrderByCourier(courierId, orderId);
        UserResponseDTO courier = authServiceClient.getUserById(courierId);
        order.setCourierId(courier.getId());
        order.setStatus(OrderStatus.PICKED_UP);

        orderRepository.save(order);

        mailService.sendStatusUpdateNotification(order);
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

        mailService.sendStatusUpdateNotification(order);
    }

    @Override
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Order not found on id : " + orderId)
        );
    }

    @Override
    public List<OrderResponseDTO> getByFilter(int page, int size, OrderFilterDto filterDto) {
        Pageable pageable = PageRequest.of(page, size);
        if (filterDto.getStartPoint() != null) {
            return getOrdersByStartingPoint(filterDto.getStartPoint(), pageable);
        }
        if (filterDto.getEndPoint() != null) {
            return getOrdersByEndingPoint(filterDto.getEndPoint(), pageable);
        }
        if (filterDto.getDate() != null) {
            Timestamp timestampParam = new Timestamp(filterDto.getDate().getTime());
            return getOrdersByDate(timestampParam, pageable);
        }
        throw new BadRequestException("Invalid filter parameters. Please provide either a valid startPoint, endPoint, or date.");
    }

    @Override
    public Order getOrderByCustomerId(Long customerId, Long orderId) {
        Optional<Order> order = orderRepository.findByCustomerIdAndId(customerId, orderId);
        if (order.isEmpty()) {
            throw new BadRequestException("Access denied");
        }
        return order.get();
    }

    @Override
    public List<OrderResponseDTO> getActiveOrders(OrderParticipantsDto dto) {
        if (dto.getCustomerId() != null) {
            return orderRepository.findCustomerActiveOrders(dto.getCustomerId())
                    .stream()
                    .map(order -> findById(order.getId()))
                    .toList();
        } else if (dto.getCourierId() != null) {
            return orderRepository.findCourierActiveOrders(dto.getCourierId())
                    .stream()
                    .map(order -> findById(order.getId()))
                    .toList();
        } else {
            return orderRepository.findAllByStatus()
                    .stream()
                    .map(order -> findById(order.getId()))
                    .toList();
        }
    }

    @Override
    public List<OrderResponseDTO> getHistoryOrders(OrderParticipantsDto dto) {
        if (dto.getCustomerId() != null) {
            return orderRepository.findCustomerHistoryOrders(dto.getCustomerId())
                    .stream()
                    .map(order -> findById(order.getId()))
                    .toList();
        } else if (dto.getCourierId() != null) {
            return orderRepository.findCourierHistoryOrders(dto.getCourierId())
                    .stream()
                    .map(order -> findById(order.getId()))
                    .toList();
        }
        throw new BadRequestException("Invalid request");
    }

    private List<OrderResponseDTO> getOrdersByCriteria(
            Function<Pageable, Page<Order>> queryFunction,
            Pageable pageable
    ) {
        return queryFunction.apply(pageable)
                .getContent()
                .stream()
                .map(this::convertEntityToResponseDTO)
                .toList();
    }

    private List<OrderResponseDTO> getOrdersByStartingPoint(String startPoint, Pageable pageable) {
        return getOrdersByCriteria(pageableParam -> orderRepository.getByStartingPoint(startPoint, pageableParam), pageable);
    }

    private List<OrderResponseDTO> getOrdersByEndingPoint(String endPoint, Pageable pageable) {
        return getOrdersByCriteria(pageableParam -> orderRepository.getByEndingPoint(endPoint, pageableParam), pageable);
    }

    private List<OrderResponseDTO> getOrdersByDate(Timestamp date, Pageable pageable) {
        return getOrdersByCriteria(pageableParam -> orderRepository.getByDate(date, pageableParam), pageable);
    }

    private OrderResponseDTO convertEntityToResponseDTO(Order order) {
        var orderResponseDTO = orderMapper.mapOrderEntityToResponse(order);
        orderResponseDTO.setCostumer(authServiceClient.getUserById(order.getCustomerId()));
        if (order.getCourierId() != null) {
            orderResponseDTO.setCourier(authServiceClient.getUserById(order.getCourierId()));
        }
        return orderResponseDTO;
    }
}
