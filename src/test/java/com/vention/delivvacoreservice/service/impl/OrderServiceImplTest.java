package com.vention.delivvacoreservice.service.impl;

import com.vention.delivvacoreservice.domain.Order;
import com.vention.delivvacoreservice.domain.OrderDestination;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
//import com.vention.delivvacoreservice.dto.response.UserResponseDTO;
import com.vention.delivvacoreservice.feign_clients.AuthServiceClient;
import com.vention.delivvacoreservice.mappers.OrderMapper;
import com.vention.delivvacoreservice.repository.OrderRepository;
import com.vention.delivvacoreservice.service.MailService;
import com.vention.delivvacoreservice.service.OrderDestinationService;
import com.vention.delivvacoreservice.utils.MapUtils;
import com.vention.general.lib.dto.response.GeolocationDTO;
import com.vention.general.lib.dto.response.OrderResponseDTO;
import com.vention.general.lib.dto.response.UserResponseDTO;
import com.vention.general.lib.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private MapUtils mapUtils;

    @Mock
    private MailService mailService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private TrackNumberGenerator trackNumberGenerator;

    @Mock
    private OrderDestinationService orderDestinationService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderCreationRequestDTO request;

    @Test
    void testCreateOrder() {
        // given
        GeolocationDTO startingPlaceDTO = mock();
        GeolocationDTO finalPlaceDTO = mock();
        UserResponseDTO userResponseDTO = mock();
        OrderDestination destination = mock();
        String trackNumber = "test";
        String date = "2023-12-12";
        Order order = mock();
        Order savedOrder = mock();
        OrderResponseDTO responseDTO = mock();
        // when
        doReturn(startingPlaceDTO).when(request).getStartingDestination();
        doReturn(finalPlaceDTO).when(request).getFinalDestination();
        doReturn(date).when(request).getScheduledDeliveryDate();
        doReturn(true).when(orderDestinationService).areDestinationsValid(anyList());
        doReturn(userResponseDTO).when(authServiceClient).getUserById(anyLong());
        doReturn(destination).when(orderDestinationService).getOrderDestinationWithValidation(any());
        doReturn(order).when(orderMapper).mapOrderRequestToEntity(any());
        doReturn(trackNumber).when(trackNumberGenerator).generateTrackNumber(any(), any());
        doReturn(savedOrder).when(orderRepository).save(any(Order.class));
        doReturn(responseDTO).when(orderMapper).mapOrderEntityToResponse(any(Order.class));
        OrderResponseDTO result = orderService.createOrder(request);
        // then
        assertSame(responseDTO, result);
        verify(request, times(1)).getFinalDestination();
        verify(request, times(1)).getStartingDestination();
        verify(orderDestinationService, times(1)).areDestinationsValid(anyList());
        verify(authServiceClient, times(1)).getUserById(anyLong());
        verify(orderDestinationService, times(2)).getOrderDestinationWithValidation(any());
        verify(orderMapper, times(1)).mapOrderRequestToEntity(any());
        verify(trackNumberGenerator, times(1)).generateTrackNumber(any(), any());
        verify(orderRepository, times(1)).save(any());
        verify(orderMapper, times(1)).mapOrderEntityToResponse(any());
    }

    @Test
    void WillOrderCreationTestThrow() {
        // given
        GeolocationDTO startingPlaceDTO = mock();
        GeolocationDTO finalPlaceDTO = mock();
        // when
        doReturn(startingPlaceDTO).when(request).getStartingDestination();
        doReturn(finalPlaceDTO).when(request).getFinalDestination();
        doReturn(false).when(orderDestinationService).areDestinationsValid(anyList());
        // then
        assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
        verify(request, times(1)).getFinalDestination();
        verify(request, times(1)).getStartingDestination();
        verify(orderDestinationService, times(1)).areDestinationsValid(anyList());
        verify(authServiceClient, never()).getUserById(anyLong());
        verify(orderDestinationService, never()).getOrderDestinationWithValidation(any());
        verify(orderMapper, never()).mapOrderRequestToEntity(any());
        verify(trackNumberGenerator, never()).generateTrackNumber(any(), any());
        verify(orderRepository, never()).save(any());
        verify(orderMapper, never()).mapOrderEntityToResponse(any());
    }
}