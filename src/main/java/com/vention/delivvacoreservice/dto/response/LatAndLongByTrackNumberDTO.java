package com.vention.delivvacoreservice.dto.response;

import com.vention.general.lib.dto.response.GeolocationDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LatAndLongByTrackNumberDTO {
    private String trackNumber;
    private List<GeolocationDTO> path;
}
