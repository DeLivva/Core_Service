package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.UserIdDTO;
import com.vention.delivvacoreservice.dto.response.LatAndLongByTrackNumberDTO;
import com.vention.delivvacoreservice.dto.response.PathByUserIdDTO;
import com.vention.delivvacoreservice.feign_clients.GeoServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/geo")
public class GeoController {
    private final GeoServiceClient client;

    @GetMapping("/{trackNumber}")
    public ResponseEntity<LatAndLongByTrackNumberDTO> getPathByTrackNumber(@PathVariable String trackNumber) {
        return client.getPathByTrackNumber(trackNumber);
    }

    @GetMapping
    public ResponseEntity<PathByUserIdDTO> getPathByUserId(@RequestBody UserIdDTO dto) {
        return client.getPathByUserId(dto);
    }
}
