package com.vention.delivvacoreservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.delivvacoreservice.dto.request.DisputeCreateRequestDTO;
import com.vention.delivvacoreservice.dto.request.GptRequestDTO;
import com.vention.delivvacoreservice.dto.request.OrderCreationRequestDTO;
import com.vention.delivvacoreservice.feign_clients.AuthServiceClient;
import com.vention.delivvacoreservice.feign_clients.DisputeClient;
import com.vention.delivvacoreservice.feign_clients.GPTClient;
import com.vention.delivvacoreservice.service.ChatGPTService;
import com.vention.delivvacoreservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private final GPTClient gptClient;
    private final OrderService orderService;
    private final AuthServiceClient authServiceClient;
    private final DisputeClient disputeClient;

    @Override
    @Scheduled(cron = "0 31 21 * * ?")
    public void populateDatabase() {
        String orderCreationPrompt = getOrderCreationPrompt();
        var ordersJson = gptClient.generatePrompt(new GptRequestDTO(orderCreationPrompt));
        List<OrderCreationRequestDTO> orderRequests = parseJsonStrToOrdersDto(ordersJson.getChoices().get(0).getMessage().getContent());
        for (var orderRequest : orderRequests) {
            var orderResponse = orderService.createOrder(orderRequest);
            orderService.approveAnOffer(getRandomCourierId(), orderResponse.getId());
            createDispute(orderResponse.getId(), orderResponse.getCostumer().getId());
        }
        System.out.println("Finished");
    }

    private void createDispute(Long orderId, Long customerId) {
        DisputeCreateRequestDTO disputeCreateRequestDTO;
        if (orderId % 7 == 0) { // as we are just creating mock data not every order should have dispute, so I am doing a logic that several orders have dispute
            disputeCreateRequestDTO = new DisputeCreateRequestDTO(orderId, 1L, customerId, getGPTCreatedDescription());
            disputeClient.create(disputeCreateRequestDTO);
        } else if (orderId % 5 == 0) {
            disputeCreateRequestDTO = new DisputeCreateRequestDTO(orderId, 2L, customerId, getGPTCreatedDescription());
            disputeClient.create(disputeCreateRequestDTO);
        }
    }

    private List<OrderCreationRequestDTO> parseJsonStrToOrdersDto(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<OrderCreationRequestDTO> orders = objectMapper
                    .readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, OrderCreationRequestDTO.class));
            for (OrderCreationRequestDTO order : orders) {
                System.out.println(order.getUserId());
                System.out.println(order.getItemDescription());
                System.out.println(order.getStartingDestination().getLatitude());
                System.out.println(order.getStartingDestination().getLongitude());
                System.out.println(order.getFinalDestination().getLatitude());
                System.out.println(order.getFinalDestination().getLongitude());
                System.out.println(order.getScheduledDeliveryDate());
            }
            return orders;
        } catch (IOException e) {
            log.warn("Parse gpt response to order");
            throw new RuntimeException("Parsing error json string to order dto" + e.getMessage());
        }
    }

    private Long getRandomCourierId() {
        var usersIdList = authServiceClient.getUsersIdList(true);
        int randomIndex = (int) (Math.random() * usersIdList.size());
        return usersIdList.get(randomIndex);
    }

    private String getOrderCreationPrompt() {
        var usersIdList = authServiceClient.getUsersIdList(false);
        return """
                {
                  "userId": 1,
                  "itemDescription": "4 boxes and 1 bag",
                  "startingDestination": {
                    "latitude": 39.7675529,
                    "longitude": 64.4231326
                  },
                  "finalDestination": {
                    "latitude": 41.3123363,
                    "longitude": 69.2787079
                  },
                  "scheduledDeliveryDate": "2024-01-15"
                }
                create list of such json containing different 15 elements
                - Write different itemDescriptions and Choose userId randomly from this list %s
                - Use valid different lat and long pairs for startingDestination and finalDestination
                - Behave like an API REST entrypoint, giving only JSON responses formatted strictly with no deviations. Get prompts like http requests from an API Client.
                - Your response should be JSON parseable by a machine.
                - Don’t give any polite introduction on your response, just JSON format""".formatted(usersIdList);
    }

    private String getGPTCreatedDescription() {
        String disputeCreationPrompt = """
                In my application, users can create dispute for their delivery if something is wrong with delivery.
                For example: My delivery became so late and some items such as glasses broke
                - Don’t give any polite introduction or conclusion on your response, just generate such kind of mock random description less than 255 chars
                - If I request you this task, generate new description everytime requested""";
        var disputeResponse = gptClient.generatePrompt(new GptRequestDTO(disputeCreationPrompt));
        return disputeResponse.getChoices().get(0).getMessage().getContent();
    }
}
