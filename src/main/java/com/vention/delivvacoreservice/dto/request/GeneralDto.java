package com.vention.delivvacoreservice.dto.request;

import com.vention.delivvacoreservice.domain.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralDto<T> {
    private T data;
    private NotificationType type;
}
