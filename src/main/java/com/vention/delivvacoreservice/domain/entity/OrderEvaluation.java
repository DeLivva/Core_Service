package com.vention.delivvacoreservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "order_evaluations")
public class OrderEvaluation extends BaseEntity {

    @Column(nullable = false)
    private Double rate;

    private String description;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderEntity order;

    @Column(nullable = false)
    private Long userId;
}
