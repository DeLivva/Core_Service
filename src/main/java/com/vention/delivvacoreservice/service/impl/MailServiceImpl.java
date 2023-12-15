package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.dto.mail.OrderMailDTO;
import com.vention.delivvacoreservice.service.MailService;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public void sendAnOffer(OrderMailDTO mailDTO) {
        // message will be sent via rabbitmq
    }
}
