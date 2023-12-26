package com.vention.delivvacoreservice.feign_clients;

import com.vention.delivvacoreservice.config.CustomErrorDecoder;
import com.vention.delivvacoreservice.dto.request.DisputeCreateRequestDTO;
import com.vention.delivvacoreservice.dto.response.DisputeResponseDTO;
import com.vention.delivvacoreservice.dto.response.DisputeTypeResponseDTO;
import com.vention.general.lib.dto.request.PaginationRequestDTO;
import com.vention.general.lib.dto.response.ResponseWithPaginationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "disputeServiceApi", url = "${cloud.dispute-service.url}", configuration = CustomErrorDecoder.class)
public interface DisputeClient {

    @PostMapping("/api/v1/disputes")
    ResponseEntity<DisputeResponseDTO> create(@RequestBody DisputeCreateRequestDTO requestDTO);

    @PutMapping("/api/v1/disputes/close/{id}")
    ResponseEntity<Void> close(@PathVariable("id") Long id);

    @GetMapping("/api/v1/disputes")
    ResponseEntity<List<DisputeResponseDTO>> getByUserId(@RequestParam Long userId);

    @GetMapping("/api/v1/disputes/all")
    ResponseEntity<ResponseWithPaginationDTO<DisputeResponseDTO>> getAll(@RequestParam PaginationRequestDTO paginationRequestDTO);

    @GetMapping("/api/v1/dispute-types")
    ResponseEntity<List<DisputeTypeResponseDTO>> getAllTypes();
}
