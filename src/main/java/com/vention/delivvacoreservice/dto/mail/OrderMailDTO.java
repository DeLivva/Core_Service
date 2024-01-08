package com.vention.delivvacoreservice.dto.mail;

import com.vention.general.lib.dto.response.UserResponseDTO;
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
public class OrderMailDTO {
    private Long id;
    private String itemDescription;
    private String startLocation;
    private String finalLocation;
    private UserResponseDTO customer;
    private UserResponseDTO courier;
    private Timestamp deliveryDate;
    private String trackNumber;
    private Sender sender;
}
