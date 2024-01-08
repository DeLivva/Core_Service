package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.InvitationResponseDTO;
import com.vention.delivvacoreservice.dto.InvitationToCourierDTO;
import com.vention.delivvacoreservice.service.OrderInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class OrderInvitationController {

    private final OrderInvitationService service;

    @GetMapping("/{order_id}")
    public ResponseEntity<List<InvitationResponseDTO>> getAllByOrderId(@PathVariable("order_id") Long orderId) {
        return ResponseEntity.ok(service.getAllByOrderId(orderId));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<Void> approveOffer(@PathVariable long id) {
        service.approveOffer(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> rejectOffer(@PathVariable long id) {
        service.rejectReject(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/courier")
    public ResponseEntity<List<InvitationToCourierDTO>> getInvitations(@RequestParam Long courierId) {
        return ResponseEntity.ok(service.getAllByCourier(courierId));
    }
}
