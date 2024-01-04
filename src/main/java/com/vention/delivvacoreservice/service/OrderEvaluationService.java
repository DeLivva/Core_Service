package com.vention.delivvacoreservice.service;

import com.vention.delivvacoreservice.dto.request.OrderEvaluationDto;
import com.vention.delivvacoreservice.dto.response.CourierRatingResponseDto;
import com.vention.delivvacoreservice.dto.response.CourierResponseDTO;
import com.vention.delivvacoreservice.dto.response.OrderEvaluationResponseDto;

import java.util.List;

public interface OrderEvaluationService {

    OrderEvaluationResponseDto create(OrderEvaluationDto orderEvaluationDto);

    List<CourierRatingResponseDto> getCouriers(int page, int size);

    List<CourierResponseDTO> filterAndSortCouriers(List<CourierResponseDTO> couriers);
}
