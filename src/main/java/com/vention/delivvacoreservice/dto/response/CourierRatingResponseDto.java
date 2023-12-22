package com.vention.delivvacoreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourierRatingResponseDto {
    private Long courierId;
    private Double rating;
    private Long quantity;
}
