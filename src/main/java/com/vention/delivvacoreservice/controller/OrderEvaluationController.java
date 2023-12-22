package com.vention.delivvacoreservice.controller;

import com.vention.delivvacoreservice.dto.request.OrderEvaluationDto;
import com.vention.delivvacoreservice.dto.response.CourierRatingResponseDto;
import com.vention.delivvacoreservice.dto.response.OrderEvaluationResponseDto;
import com.vention.delivvacoreservice.service.OrderEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluations")
@RequiredArgsConstructor
public class OrderEvaluationController {

    private final OrderEvaluationService orderEvaluationService;

    @PostMapping
    public ResponseEntity<OrderEvaluationResponseDto> create(@RequestBody OrderEvaluationDto orderEvaluationDto) {
        return new ResponseEntity<>(orderEvaluationService.create(orderEvaluationDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourierRatingResponseDto>> getCourierList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
            ) {
        return ResponseEntity.ok(orderEvaluationService.getCouriers(page, size));
    }
}
