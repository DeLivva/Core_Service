package com.vention.delivvacoreservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PathByUserIdDTO {
    private Long userId;
    private List<LatAndLongByTrackNumberDTO> trackNumbers;
}
