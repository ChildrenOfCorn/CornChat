package ru.cft.chuldrenofcorn.cornchat.data.db;

import java.util.LinkedList;
import java.util.List;

import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * Created by grishberg on 27.08.16.
 */
public class ChatMessageRepositoryArray implements ChatMessageRepository {
    private static final String TAG = ChatMessageRepositoryArray.class.getSimpleName();
    private final LinkedList<ChatMessage> list;

    public ChatMessageRepositoryArray() {
        list = new LinkedList<>();
    }

    @Override
    public List<ChatMessage> getMessages(final String userId) {
        return list;
    }

    @Override
    public void add(final ChatMessage message, final String userId) {
        list.add(0, message);
    }

    @Override
    public void deleteMessages(final String userId) {
        list.clear();
    }
}
