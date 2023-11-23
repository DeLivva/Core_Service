package com.vention.delivvacoreservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "orders")
public class OrderEntity extends BaseEntity {

    @Column(nullable = false)
    private Long finalDestinationId;

    @Column(nullable = false)
    private Long startingDestinationId;

    @Column(nullable = false)
    private Long customerId;

    private Long courierId;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    private List<OrderEvaluation> evaluations;

    private String itemDescription;

    private Timestamp deliveryStartedDate;

    private Timestamp deliveryFinishedDate;
}
