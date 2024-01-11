package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.dto.mail.OrderMailDTO;
import com.vention.delivvacoreservice.dto.mail.Sender;
import com.vention.delivvacoreservice.dto.request.NotificationDTO;
import com.vention.delivvacoreservice.feign_clients.AuthServiceClient;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.service.MailService;
import com.vention.delivvacoreservice.service.rabbitmq.producer.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final AuthServiceClient authServiceClient;
    private final RabbitMQProducer producer;
    private final OrderMapper orderMapper;

    @Override
    public void sendAnOffer(OrderMailDTO mailDTO) {
        Map<String, Object> data = orderMapper.orderMailDTOToMap(mailDTO);
        data.put("userId", mailDTO.getCourier().getId());
        String emailTo;
        // courierId and customerId added to set id for offer response link
        if (mailDTO.getSender().equals(Sender.CUSTOMER)) {
            data.put("userSender", mailDTO.getCustomer());
            emailTo = mailDTO.getCourier().getEmail();
        } else {
            data.put("userSender", mailDTO.getCourier());
            emailTo = mailDTO.getCustomer().getEmail();
        }
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .title("Order offer")
                .email(emailTo)
                .data(data)
                .build();
        producer.sendOrderOffer(notificationDTO);
    }

    @Override
    public void sendStatusUpdateNotification(Order order) {
        String customerEmail = authServiceClient.getUserById(order.getCustomerId()).getEmail();
        String courierEmail = authServiceClient.getUserById(order.getCourierId()).getEmail();

        Map<String, Object> data = new HashMap<>();
        data.put("trackNumber", order.getTrackNumber());
        data.put("driverEmail", courierEmail);
        data.put("status", order.getStatus());
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .title("Order Status Update")
                .email(customerEmail)
                .data(data)
                .build();
        producer.sendOrderStatus(notificationDTO);
    }
}
