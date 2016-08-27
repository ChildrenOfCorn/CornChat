package ru.cft.chuldrenofcorn.cornchat.data.db;

import java.util.List;

import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * Created by grishberg on 27.08.16.
 */
public interface ChatMessageRepository {
    List<ChatMessage> getMessages(String userId);
    void add(ChatMessage message, String userId);
    void deleteMessages(String userId);
}
