package com.vention.delivvacoreservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderDestinationsDTO {
    private Long userId;
    private String trackNumber;
    private Double startLatitude;
    private Double startLongitude;
    private Double finalLatitude;
    private Double finalLongitude;
}
