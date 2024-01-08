package com.vention.delivvacoreservice.dto;

import com.vention.delivvacoreservice.enums.InvitationStatus;
import com.vention.general.lib.dto.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class InvitationResponseDTO {
    private Long invitationId;
    private Long orderId;
    private UserResponseDTO courier;
    private InvitationStatus status;
    private Timestamp createdAt;
}
