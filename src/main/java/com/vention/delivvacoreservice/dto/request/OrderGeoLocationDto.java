package com.vention.delivvacoreservice.dto.request;

import com.vention.delivvacoreservice.dto.GeolocationDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderGeoLocationDto extends GeolocationDTO {
    private String trackNumber;
}
