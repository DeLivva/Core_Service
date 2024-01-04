package com.vention.delivvacoreservice.dto.response;

import com.vention.general.lib.dto.response.UserResponseDTO;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class CourierResponseDTO extends UserResponseDTO implements Comparable<CourierResponseDTO> {
    private Integer rate;

    @Override
    public int compareTo(@NotNull CourierResponseDTO o) {
        return Integer.compare(o.rate, this.rate);
    }
}
