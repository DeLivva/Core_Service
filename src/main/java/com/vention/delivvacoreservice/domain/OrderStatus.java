package com.vention.delivvacoreservice.domain;

public enum OrderStatus {
    CREATED,
    PICKED_UP,
    IN_PROGRESS,
    REJECTED_BY_COURIER,
    REJECTED_BY_CUSTOMER,
    DISPUTE_OPENED,
    UNDER_CONSIDERATION,
    DISPUTE_CLOSED_CUSTOMER_WIN,
    DISPUTE_CLOSED_COURIER_WIN,
    DONE
}
