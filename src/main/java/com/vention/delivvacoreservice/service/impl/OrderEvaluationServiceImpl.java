package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.dto.request.OrderEvaluationDto;
import com.vention.delivvacoreservice.dto.response.CourierRatingResponseDto;
import com.vention.delivvacoreservice.dto.response.OrderEvaluationResponseDto;
import com.vention.delivvacoreservice.mappers.OrderEvaluationMapper;
import com.vention.delivvacoreservice.repository.OrderEvaluationRepository;
import com.vention.delivvacoreservice.service.OrderEvaluationService;
import com.vention.delivvacoreservice.service.OrderService;
import com.vention.general.lib.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderEvaluationServiceImpl implements OrderEvaluationService {

    private final OrderEvaluationRepository orderEvaluationRepository;
    private final OrderEvaluationMapper mapper;
    private final OrderService orderService;

    @Override
    public OrderEvaluationResponseDto create(OrderEvaluationDto orderEvaluationDto) {
        Order order = orderService.getOrderByCustomerId(orderEvaluationDto.getUserId(), orderEvaluationDto.getOrderId());

        if (orderEvaluationRepository.findByOrderId(orderEvaluationDto.getOrderId()).isPresent()) {
            throw new BadRequestException("You have already rated this order");
        }
        var evaluation = mapper.convertDtoToEntity(orderEvaluationDto);
        evaluation.setOrder(order);

        evaluation = orderEvaluationRepository.save(evaluation);
        // here will be method which sends notification about rating to courier
        return mapper.convertEntityToDto(evaluation, order.getCourierId());
    }

    @Override
    public List<CourierRatingResponseDto> getCouriers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> couriers = orderEvaluationRepository.couriers(pageable);
        return convertToDto(couriers);
    }

    private List<CourierRatingResponseDto> convertToDto(Page<Object[]> resultList) {
        return resultList.stream()
                .map(row -> new CourierRatingResponseDto((Long) row[0], (Double) row[1], (Long) row[2]))
                .toList();
    }
}