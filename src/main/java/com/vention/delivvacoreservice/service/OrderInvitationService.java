package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.dto.InvitationResponseDTO;
import com.vention.delivvacoreservice.dto.InvitationToCourierDTO;

import java.util.List;

public interface OrderInvitationService {
    List<InvitationResponseDTO> getAllByOrderId(Long orderId);

    void approveOffer(long id);

    void rejectReject(long id);

    List<InvitationToCourierDTO> getAllByCourier(Long courierId);
}
