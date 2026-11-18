package com.taskit.rpadservice.Infrastucture.Outbound.Messaging;

import com.taskit.rpadservice.Infrastucture.MessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class MessageClient {

    private final RestClient restClient;

    public MessageClient(RestClient.Builder builder, @Value("${chat.service.url}") String chatServiceUrl) {
        String normalizedUrl = chatServiceUrl.trim();
        if ((normalizedUrl.startsWith("\"") && normalizedUrl.endsWith("\""))
                || (normalizedUrl.startsWith("'") && normalizedUrl.endsWith("'"))) {
            normalizedUrl = normalizedUrl.substring(1, normalizedUrl.length() - 1);
        }
        this.restClient = builder
                .baseUrl(normalizedUrl)
                .build();
    }

    public List<MessageDTO> getMessages(String taskId) {
        List<MessageDTO> messages = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/chats/messages")
                        .queryParam("taskId", taskId)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<MessageDTO>>() {});
        if (messages == null) {
            throw new IllegalStateException("Chat service returned an empty response body");
        }
        return messages;
    }
}
