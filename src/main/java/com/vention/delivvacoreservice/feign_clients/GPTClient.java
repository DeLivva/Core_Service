package com.vention.delivvacoreservice.feign_clients;

import com.vention.delivvacoreservice.config.GPTFeignClientConfiguration;
import com.vention.delivvacoreservice.dto.request.GptRequestDTO;
import com.vention.delivvacoreservice.dto.response.ChatCompletionResponseDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ChatGPTAPI", url = "${cloud.gpt.url}", configuration = {GPTFeignClientConfiguration.class})
public interface GPTClient {
    @PostMapping()
    @Headers("Connect-TimeOut: 180000")
    ChatCompletionResponseDTO generatePrompt(@RequestBody GptRequestDTO requestDTO);
}
