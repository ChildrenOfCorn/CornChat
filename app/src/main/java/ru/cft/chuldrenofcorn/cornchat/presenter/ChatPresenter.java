package ru.cft.chuldrenofcorn.cornchat.presenter;

import android.content.Context;

import java.util.ArrayList;

import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.dto.ChatMessage;

/**
 * Created by azhukov on 26/08/16.
 */
public class ChatPresenter {

    private Context context;

    private ArrayList<ChatMessage> messages;
    private ChatAdapter chatAdapter;

    public ChatPresenter(final Context context) {
        this.context = context;

        messages = new ArrayList<>(10);
        chatAdapter = new ChatAdapter(context);
    }

    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    public void sendMessage(final String messageText) {
        messages.add(new ChatMessage("vasya", messageText, "4.20", true));
        chatAdapter.setMessages(messages);
    }
}
