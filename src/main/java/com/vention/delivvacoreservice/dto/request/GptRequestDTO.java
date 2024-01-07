package com.vention.delivvacoreservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GptRequestDTO {
    @JsonProperty("model")
    private String model = "gpt-3.5-turbo";

    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("temperature")
    private double temperature = 1;

    public GptRequestDTO(String prompt) {
        messages = List.of(new Message(prompt));
    }

    @Data
    public static class Message {
        @JsonProperty("role")
        private String role = "user";

        @JsonProperty("content")
        private String content;

        public Message(String content) {
            this.content = content;
        }
    }
}
