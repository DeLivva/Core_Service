package com.vention.delivvacoreservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "starting_destination_id", referencedColumnName = "id")
    private OrderDestination startingDestination;

    @ManyToOne
    @JoinColumn(name = "final_destination_id", referencedColumnName = "id")
    private OrderDestination finalDestination;

    @Column(nullable = false)
    private Long customerId;

    private Long courierId;

    @Column(nullable = false)
    private Timestamp deliveryDate;

    @Column(nullable = false)
    private String trackNumber;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;

    @OneToMany(mappedBy = "order")
    private List<OrderEvaluation> evaluations;

    private String itemDescription;

    private Timestamp deliveryStartedAt;

    private Timestamp deliveryFinishedAt;
}

