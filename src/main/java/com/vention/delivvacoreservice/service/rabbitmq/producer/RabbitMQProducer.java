package com.vention.delivvacoreservice.service.rabbitmq.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.delivvacoreservice.domain.NotificationType;
import com.vention.delivvacoreservice.dto.request.GeneralDto;
import com.vention.delivvacoreservice.dto.request.NotificationDTO;
import com.vention.delivvacoreservice.dto.request.OrderGeoLocationDto;
import com.vention.general.lib.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.exchange.notification}")
    private String exchangeNotification;

    @Value("${rabbitmq.routing.key.geo}")
    private String routingKeyGeoService;

    @Value("${rabbitmq.routing.key.core}")
    private String routingKeyToNotificationService;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    public void sendGeoPosition(OrderGeoLocationDto geolocationDTO) {
        sendNotification(geolocationDTO, NotificationType.GEO_POSITION, exchange, routingKeyGeoService);
    }

    public void sendOrderStatus(NotificationDTO notificationDTO) {
        sendNotification(notificationDTO, NotificationType.ORDER_STATUS_CHANGE, exchangeNotification, routingKeyToNotificationService);
    }

    public void sendOrderOffer(NotificationDTO notificationDTO) {
        sendNotification(notificationDTO, NotificationType.ORDER_OFFER, exchangeNotification, routingKeyToNotificationService);
    }

    public void sendNotification(Object body, NotificationType notificationType, String exchange, String routingKey) {
        GeneralDto<Object> generalDto = GeneralDto.builder().body(body).type(notificationType).build();
        try {
            String json = objectMapper.writeValueAsString(generalDto);
            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    json,
                    message -> {
                        MessageProperties properties = message.getMessageProperties();
                        properties.setContentType("application/json");
                        return message;
                    });
        } catch (IOException e) {
            log.error("Error occurred while sending notification: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
