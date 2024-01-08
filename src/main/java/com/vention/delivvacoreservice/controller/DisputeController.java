package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.request.DisputeCreateRequestDTO;
import com.vention.delivvacoreservice.dto.response.DisputeResponseDTO;
import com.vention.delivvacoreservice.dto.response.DisputeResponseWithUser;
import com.vention.delivvacoreservice.feign_clients.AuthServiceClient;
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
    private final AuthServiceClient authServiceClient;

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

    @GetMapping("/{order_id}")
    public ResponseEntity<DisputeResponseDTO> getByOrderId(@PathVariable("order_id") Long userId) {
        return disputeClient.getByOrderId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWithPaginationDTO<DisputeResponseWithUser>> getAll(PaginationRequestDTO paginationRequestDTO) {
        var disputesResponse = disputeClient
                .getAll(paginationRequestDTO.getPage(), paginationRequestDTO.getSize()).getBody();
        List<DisputeResponseWithUser> disputes = disputesResponse.getData().stream().map(disputeResponseDTO -> {
            DisputeResponseWithUser disputeResponseWithUser = new DisputeResponseWithUser(disputeResponseDTO);
            disputeResponseWithUser.setUser(authServiceClient.getUserById(disputeResponseDTO.getUserId()));
            return disputeResponseWithUser;
        }).toList();
        return ResponseEntity.ok(new ResponseWithPaginationDTO<>(disputesResponse.getCurrentPage(), disputesResponse.getTotalPages(), disputesResponse.getTotalItems(), disputesResponse.getPageSize(), disputes));
    }
}
