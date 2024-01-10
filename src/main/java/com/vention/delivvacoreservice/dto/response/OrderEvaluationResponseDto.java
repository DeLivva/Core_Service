package com.vention.delivvacoreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvaluationResponseDto {
    private Long id;
    private String rate;
    private Long orderId;
    private Long userId;
    private Long courierId;
}
