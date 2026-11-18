package com.taskit.rpadservice.Domain.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public record Message(
        String messageId,
        String chatId,
        String senderId,
        String content,
        LocalDateTime timestamp
) {
    public Message {
        Objects.requireNonNull(messageId, "messageId must not be null");
        Objects.requireNonNull(chatId, "chatId must not be null");
        Objects.requireNonNull(senderId, "senderId must not be null");
        Objects.requireNonNull(content, "content must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
    }
}
