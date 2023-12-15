package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderDestinationRepository;
import com.vention.general.lib.dto.response.GeolocationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderDestinationServiceImplTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderDestinationRepository orderDestinationRepository;

    @InjectMocks
    private OrderDestinationServiceImpl orderDestinationService;

    @Test
    void testGetOrderDestinationWithValidation() {
        // given
        GeolocationDTO geolocationDTO = new GeolocationDTO(1234D, 25343D);
        OrderDestination orderDestination = mock();
        // when
        doReturn(Optional.of(orderDestination))
                .when(orderDestinationRepository)
                .findByLongitudeAndLatitude(anyDouble(), anyDouble());
        OrderDestination result = orderDestinationService.getOrderDestinationWithValidation(geolocationDTO);
        assertSame(orderDestination, result);
        verify(orderDestinationRepository, times(1)).findByLongitudeAndLatitude(
                anyDouble(),
                anyDouble()
        );
        verify(orderDestinationRepository, never()).save(any());
    }

    @Test
    void testGetOrderDestinationWithValidation2() {
        // given
        GeolocationDTO geolocationDTO = new GeolocationDTO(1234D, 25343D);
        OrderDestination orderDestination = mock();
        // when
        doReturn(Optional.empty())
                .when(orderDestinationRepository)
                .findByLongitudeAndLatitude(anyDouble(), anyDouble());
        doReturn(orderDestination).when(orderDestinationRepository).save(any());
        OrderDestination result = orderDestinationService.getOrderDestinationWithValidation(geolocationDTO);
        assertSame(orderDestination, result);
        verify(orderDestinationRepository, times(1)).findByLongitudeAndLatitude(
                anyDouble(),
                anyDouble()
        );
        verify(orderDestinationRepository, times(1)).save(any());
    }

    @Test
    void testAreDestinationsValid() {
        // given
        List<GeolocationDTO> geolocations = Arrays.asList(
                new GeolocationDTO(12d, 34d),
                new GeolocationDTO(4354D, 2343D)
        );
        // when
        boolean result = orderDestinationService.areDestinationsValid(geolocations);
        // then
        assertTrue(result);
    }

    @Test
    void testAreDestinationsValid2() {
        // given
        List<GeolocationDTO> geolocations = Arrays.asList(
                new GeolocationDTO(12d, 34d),
                new GeolocationDTO(4354D, null)
        );
        // when
        boolean result = orderDestinationService.areDestinationsValid(geolocations);
        // then
        assertFalse(result);
    }
}