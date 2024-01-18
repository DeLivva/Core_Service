package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.OrderOfferDTO;
import com.vention.delivvacoreservice.dto.request.OrderFilterDto;
import com.vention.delivvacoreservice.dto.request.OrderParticipantsDto;
import com.vention.delivvacoreservice.dto.OrderStatusDTO;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.request.TrackNumberResponseDTO;
import com.vention.delivvacoreservice.dto.response.DiagramResponseDTO;
import com.vention.delivvacoreservice.service.OrderService;
import com.vention.general.lib.dto.response.OrderResponseDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getByIdWithAddress(id));
    }

    @PutMapping("/status")
    public ResponseEntity<Void> setStatus(@RequestBody @Valid OrderStatusDTO status) {
        orderService.setStatus(status.getId(), OrderStatus.valueOf(status.getStatus()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/offer-by-courier")
    public ResponseEntity<Void> deliveryOfferByCourier(@RequestBody @Valid OrderOfferDTO dto) {
        orderService.offerTheDelivery(false, dto.getUserId(), dto.getOrderId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/offer-by-customer")
    public ResponseEntity<Void> deliveryOfferByCustomer(@RequestBody @Valid OrderOfferDTO dto) {
        orderService.offerTheDelivery(true, dto.getUserId(), dto.getOrderId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/approve-offer")
    public ResponseEntity<Void> approveAnOffer(
            @RequestParam Long userId,
            @RequestParam Long orderId) {
        orderService.approveAnOffer(userId, orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> rejectTheOrder(@Valid @RequestBody OrderOfferDTO dto) {
        orderService.rejectAnOrder(dto.getUserId(), dto.getOrderId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> filter(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            OrderFilterDto filterDto
            ) {
        return ResponseEntity.ok(orderService.getByFilter(page, size, filterDto));
    }

    @GetMapping("/get-active-orders")
    public ResponseEntity<List<OrderResponseDTO>> getActiveOrders(OrderParticipantsDto dto){
        return ResponseEntity.ok(orderService.getActiveOrders(dto));
    }

    @GetMapping("/get-history-orders")
    public ResponseEntity<List<OrderResponseDTO>> getHistoryOrders(OrderParticipantsDto dto){
        return ResponseEntity.ok(orderService.getHistoryOrders(dto));
    }

    @PutMapping("/finish-delivery")
    public ResponseEntity<Void> finishDeliveryByCourier(@RequestParam Long orderId) {
        orderService.finishOrderByCourier(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/track-number")
    public ResponseEntity<TrackNumberResponseDTO> getTrackNumberByOrderId(@RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getTrackNumberByOrderId(orderId));
    }

    @GetMapping("/user-diagram")
    public ResponseEntity<List<DiagramResponseDTO>> getUserDiagramData(@RequestParam Long userId) {
        return ResponseEntity.ok(orderService.getUserDiagramData(userId));
    }

    @GetMapping("/admin-diagram")
    public ResponseEntity<List<DiagramResponseDTO>> getAdminDiagramData() {
        return ResponseEntity.ok(orderService.getAdminDiagramData());
    }
}
