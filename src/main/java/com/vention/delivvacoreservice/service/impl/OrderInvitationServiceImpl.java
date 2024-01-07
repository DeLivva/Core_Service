package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.domain.OrderInvitation;
import com.vention.delivvacoreservice.dto.InvitationResponseDTO;
import com.vention.delivvacoreservice.dto.InvitationToCourierDTO;
import com.vention.delivvacoreservice.enums.InvitationStatus;
import com.vention.delivvacoreservice.feign_clients.AuthServiceClient;
import com.vention.delivvacoreservice.repository.OrderInvitationsRepository;
import com.vention.delivvacoreservice.repository.OrderRepository;
import com.vention.delivvacoreservice.service.OrderInvitationService;
import com.vention.general.lib.dto.response.UserResponseDTO;
import com.vention.general.lib.enums.OrderStatus;
import com.vention.general.lib.exceptions.BadRequestException;
import com.vention.general.lib.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderInvitationServiceImpl implements OrderInvitationService {

    private final OrderInvitationsRepository repository;
    private final AuthServiceClient authServiceClient;
    private final OrderRepository orderRepository;

    @Override
    public List<InvitationResponseDTO> getAllByOrderId(Long orderId) {
        return repository.findByOrderIdOrderByCreatedAtDesc(orderId).stream().map(orderInvitation -> {
            var courier = authServiceClient.getUserById(orderId);
            return new InvitationResponseDTO(orderInvitation.getId(), orderId, courier, orderInvitation.getStatus(), orderInvitation.getCreatedAt());
        }).toList();
    }

    @Override
    @Transactional
    public void approveOffer(long id) {
        OrderInvitation invitation = repository.findById(id).orElseThrow(() -> new DataNotFoundException("invitation not found with id : " + id));

        Order order = getOrderById(invitation.getOrderId());
        if (order.getCourierId() != null) {
            throw new BadRequestException("Order has already been assigned to the courier");
        }
        repository.approveOrderByCourier(invitation.getToUserId(), invitation.getOrderId());
        UserResponseDTO courier = authServiceClient.getUserById(invitation.getToUserId());
        order.setCourierId(courier.getId());
        order.setStatus(OrderStatus.PICKED_UP);
        orderRepository.save(order);
    }

    @Override
    public void rejectReject(long id) {
        OrderInvitation invitation = repository.findById(id).orElseThrow(() -> new DataNotFoundException("invitation not found with id : " + id));
        invitation.setStatus(InvitationStatus.REJECTED);
        repository.save(invitation);
    }

    @Override
    public List<InvitationToCourierDTO> getAllByCourier(Long courierId) {
        return repository.findAllByToUserIdOrderByCreatedAtDesc(courierId).stream().map(invitation -> {
            var customer = authServiceClient.getUserById(invitation.getFromUserId());
            return new InvitationToCourierDTO(invitation.getId(), invitation.getOrderId(), customer, invitation.getStatus(), invitation.getCreatedAt());
        }).toList();
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Order not found on id : " + orderId)
        );
    }
}
