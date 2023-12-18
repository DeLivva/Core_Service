package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.request.DisputeCreateRequestDTO;
import com.vention.delivvacoreservice.dto.response.DisputeResponseDTO;
import com.vention.delivvacoreservice.feign_clients.DisputeClient;
import com.vention.general.lib.dto.request.PaginationRequestDTO;
import com.vention.general.lib.dto.response.ResponseWithPaginationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/disputes")
public class DisputeController {

    private final DisputeClient disputeClient;

    @PostMapping
    public ResponseEntity<DisputeResponseDTO> create(@RequestBody DisputeCreateRequestDTO requestDTO) {
        return disputeClient.create(requestDTO);
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<Void> close(@PathVariable("id") Long id) {
        return disputeClient.close(id);
    }

    @GetMapping
    public ResponseEntity<List<DisputeResponseDTO>> getByUserId(@RequestParam("userId") Long userId) {
        return disputeClient.getByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWithPaginationDTO<DisputeResponseDTO>> getAll(PaginationRequestDTO paginationRequestDTO) {
        return disputeClient.getAll(paginationRequestDTO);
    }
}
