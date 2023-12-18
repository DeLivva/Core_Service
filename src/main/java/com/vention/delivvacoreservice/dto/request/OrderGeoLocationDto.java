package com.vention.delivvacoreservice.dto.request;

import com.vention.general.lib.dto.response.GeolocationDTO;
import lombok.AllArgsConstructor;
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
