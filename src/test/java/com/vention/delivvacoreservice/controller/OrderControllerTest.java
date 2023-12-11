package com.vention.delivvacoreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.delivvacoreservice.dto.GeolocationDTO;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.dto.response.OrderResponseDTO;
import com.vention.delivvacoreservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testCreateOrder() throws Exception {
        // given
        OrderCreationRequestDTO orderCreationRequestDTO = new OrderCreationRequestDTO(
                1L,
                "test",
                new GeolocationDTO(23D, 23D),
                new GeolocationDTO(23D, 43D),
                "2023-12-12"
        );
        OrderResponseDTO responseDTO = mock();
        // when
        String jsonData = objectMapper.writeValueAsString(orderCreationRequestDTO);
        doReturn(responseDTO).when(orderService).createOrder(any());
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrder2() throws Exception {
        // given
        OrderCreationRequestDTO orderCreationRequestDTO = new OrderCreationRequestDTO(
                1L,
                "test",
                null,
                new GeolocationDTO(23D, 43D),
                "2023-12-12"
        );
        // when
        String jsonData = objectMapper.writeValueAsString(orderCreationRequestDTO);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(orderService, never()).createOrder(any());
    }

    @Test
    void testCreateOrder3() throws Exception {
        // given
        OrderCreationRequestDTO orderCreationRequestDTO = new OrderCreationRequestDTO(
                1L,
                "test",
                new GeolocationDTO(23D, 23D),
                new GeolocationDTO(23D, 43D),
                "2023/12/12"
        );
        // when
        String jsonData = objectMapper.writeValueAsString(orderCreationRequestDTO);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(orderService, never()).createOrder(any());
    }
}