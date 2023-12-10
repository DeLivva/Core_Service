package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;
import com.vention.delivvacoreservice.service.OrderService;
import com.vention.general.lib.enums.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<OrderStatus> getStatus(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getStatus(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> setStatus(@PathVariable("id") Long id, OrderStatus status) {
        orderService.setStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
