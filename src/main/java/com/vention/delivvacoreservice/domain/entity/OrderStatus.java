package com.vention.delivvacoreservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "order_status")
public class OrderStatus extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
