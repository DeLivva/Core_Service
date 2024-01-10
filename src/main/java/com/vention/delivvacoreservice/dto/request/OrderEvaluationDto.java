package com.vention.delivvacoreservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvaluationDto {
    @NotNull
    private Long orderId;

    @NotNull
    private Long userId;

    @NotNull
    private Double rate;
}