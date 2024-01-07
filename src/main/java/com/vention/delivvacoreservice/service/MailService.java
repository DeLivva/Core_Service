package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.dto.mail.OrderMailDTO;

public interface MailService {

    void sendAnOffer(OrderMailDTO mailDTO);

    void sendStatusUpdateNotification(Order order);
}
