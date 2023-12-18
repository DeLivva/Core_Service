package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.response.DisputeTypeResponseDTO;
import com.vention.delivvacoreservice.feign_clients.DisputeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dispute-types")
public class DisputeTypeController {

    public DisputeClient disputeClient;

    @GetMapping
    public ResponseEntity<List<DisputeTypeResponseDTO>> getAll() {
        return disputeClient.getAllTypes();
    }
}
