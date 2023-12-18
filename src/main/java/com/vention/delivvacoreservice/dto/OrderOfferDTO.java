package com.vention.delivvacoreservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class OrderOfferDTO {
    @NotNull
    @Min(value = 1)
    private Long courierId;
    @NotNull
    @Min(value = 1)
    private Long orderId;
}