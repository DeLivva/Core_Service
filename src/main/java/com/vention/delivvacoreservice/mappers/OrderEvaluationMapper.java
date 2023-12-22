package com.vention.delivvacoreservice.mappers;

import com.vention.delivvacoreservice.domain.OrderEvaluation;
import com.vention.delivvacoreservice.dto.request.OrderEvaluationDto;
import com.vention.delivvacoreservice.dto.response.OrderEvaluationResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderEvaluationMapper {
    @Mapping(target = "orderId", source = "evaluation.order.id")
    OrderEvaluationResponseDto convertEntityToDto(OrderEvaluation evaluation, Long courierId);

    OrderEvaluation convertDtoToEntity(OrderEvaluationDto orderEvaluationDto);
}
