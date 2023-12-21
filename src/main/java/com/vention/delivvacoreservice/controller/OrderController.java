package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.OrderOfferDTO;
import com.vention.delivvacoreservice.dto.OrderStatusDTO;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.general.lib.dto.response.OrderResponseDTO;
import com.vention.delivvacoreservice.service.OrderService;
import com.vention.general.lib.enums.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.util.Date;
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
    public ResponseEntity<OrderResponseDTO> getStatus(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> setStatus(@PathVariable("id") Long id, @RequestBody @Valid OrderStatusDTO status) {
        orderService.setStatus(id, OrderStatus.valueOf(status.getStatus()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/offer-by-courier")
    public ResponseEntity<Void> deliveryOfferByCourier(@RequestBody @Valid OrderOfferDTO dto) {
        orderService.offerTheDelivery(false, dto.getCourierId(), dto.getOrderId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/offer-by-customer")
    public ResponseEntity<Void> deliveryOfferByCustomer(@RequestBody @Valid OrderOfferDTO dto) {
        orderService.offerTheDelivery(true, dto.getCourierId(), dto.getOrderId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/approve-offer")
    public ResponseEntity<Void> approveAnOffer(@RequestBody @Valid OrderOfferDTO dto) {
      orderService.approveAnOffer(dto.getCourierId(), dto.getOrderId());
      return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> rejectTheOrder(@RequestBody @Valid OrderOfferDTO dto) {
        orderService.rejectAnOrder(dto.getCourierId(), dto.getOrderId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrderList() {
        return ResponseEntity.ok(orderService.getOrderList());
    }

    @GetMapping("/order-filter")
    public ResponseEntity<List<OrderResponseDTO>> filter(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String startPoint,
            @RequestParam(required = false) String endPoint,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date date
    ) {
        if (page != 0) {
            page = page - 1;
        }
        return orderService.getByFilter(page, size, startPoint, endPoint, date);
    }
}
