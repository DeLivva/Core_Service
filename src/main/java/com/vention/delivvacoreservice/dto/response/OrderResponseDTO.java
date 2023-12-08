package com.vention.delivvacoreservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vention.delivvacoreservice.dto.GeolocationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private String itemDescription;
    private GeolocationDTO startingDestination;
    private GeolocationDTO finalDestination;
    private UserResponseDTO costumer;
    private UserResponseDTO courier;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp deliveryDate;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp deliveryStartedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp deliveryFinishedAt;
}
