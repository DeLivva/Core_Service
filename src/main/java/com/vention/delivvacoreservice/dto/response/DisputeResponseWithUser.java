package com.vention.delivvacoreservice.dto.response;

import com.vention.general.lib.dto.response.UserResponseDTO;
import com.vention.general.lib.enums.OrderStatus;
import lombok.Data;

@Data
public class DisputeResponseWithUser {
    private Long id;
    private String description;
    private String disputeTypeName;
    private Long orderId;
    private UserResponseDTO user;
    private OrderStatus status;

    public DisputeResponseWithUser(DisputeResponseDTO disputeResponseDTO) {
        id = disputeResponseDTO.getId();
        description = disputeResponseDTO.getDescription();
        orderId = disputeResponseDTO.getOrderId();
        disputeTypeName = disputeResponseDTO.getDisputeTypeName();
        status = disputeResponseDTO.getStatus();
    }
}
