package com.vention.delivvacoreservice.dto.request;

import com.vention.delivvacoreservice.dto.GeolocationDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequestDTO {
    @NotNull
    @Min(value = 1, message = "User id can be neither null nor negative")
    private Long userId;

    @NotBlank(message = "itemDescription can be neither blank nor null")
    private String itemDescription;

    @NotNull(message = "starting destination cannot be null")
    private GeolocationDTO startingDestination;

    @NotNull(message = "final destination cannot be null")
    private GeolocationDTO finalDestination;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date format should be yyyy-MM-dd")
    private String scheduledDeliveryDate;
}
