package ru.cft.chuldrenofcorn.cornchat.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;
import ru.cft.chuldrenofcorn.cornchat.xmpp.MessageConsumer;

/**
 * User: azhukov
 * Date: 26.08.2016
 * Time: 20:18
 */
public class ChatPresenter {

    private static final int MESSAGE_LIST_EXPECTED_SIZE = 10;
    private final Context context;
    @Getter private XmppMessageHandler messageHandler;
    private ArrayList<ChatMessage> messages;
    private ChatAdapter chatAdapter;

    public ChatPresenter(final Context context) {
        this.context = context;

        messages = new ArrayList<>(MESSAGE_LIST_EXPECTED_SIZE);
        chatAdapter = new ChatAdapter(context);
    }

    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    public void sendMessage(final String messageText) {
        messages.add(new ChatMessage("vasya", messageText, new Date(), true));
        chatAdapter.setMessages(messages);
    }
}
