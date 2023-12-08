package com.vention.delivvacoreservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "order_destinations")
public class OrderDestination extends BaseEntity {
    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;
}
