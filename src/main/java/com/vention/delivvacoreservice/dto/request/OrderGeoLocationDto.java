package com.vention.delivvacoreservice.dto.request;

import com.vention.delivvacoreservice.dto.GeolocationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderGeoLocationDto extends GeolocationDTO {
    private String trackNumber;
}
