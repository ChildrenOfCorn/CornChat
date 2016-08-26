package ru.cft.chuldrenofcorn.cornchat.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

import ru.cft.chuldrenofcorn.cornchat.dto.ChatMessage;

/**
 * Created by azhukov on 26/08/16.
 */
public class ChatAdapter extends BaseAdapter {

    private ArrayList<ChatMessage> messages;

    public ChatAdapter() {
    }

    public ChatAdapter(final ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(final int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public View getView(final int i, final View view, final ViewGroup viewGroup) {
        return null;
    }

    public void add(final ChatMessage chatMessage) {
        messages.add(chatMessage);
        notifyDataSetChanged();
    }
}
