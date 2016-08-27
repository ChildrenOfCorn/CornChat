package ru.cft.chuldrenofcorn.cornchat.data.db;

import java.util.List;

import javax.inject.Inject;

import ru.cft.chuldrenofcorn.cornchat.App;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * Created by grishberg on 27.08.16.
 */
public class ChatMessageRepositoryDb implements ChatMessageRepository {
    private static final String TAG = ChatMessageRepositoryDb.class.getSimpleName();

    @Inject
    ChatMessageDao dao;

    public ChatMessageRepositoryDb() {
        App.getAppComponent().inject(this);
    }

    @Override
    public List<ChatMessage> getMessages(final String userId) {
        return dao.getMessagesByUserId(userId);
    }

    @Override
    public void add(final ChatMessage message, final String userId) {
        dao.add(message, userId);
    }

    @Override
    public void deleteMessages(final String userId) {
        dao.deleteByUserId(userId);
    }
}
