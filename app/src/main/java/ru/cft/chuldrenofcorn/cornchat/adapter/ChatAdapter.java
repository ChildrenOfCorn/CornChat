package ru.cft.chuldrenofcorn.cornchat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.cft.chuldrenofcorn.cornchat.R;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * User: azhukov
 * Date: 26.08.2016
 * Time: 21:05
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<ChatMessage> messages = new ArrayList<>(10);

    public ChatAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(final List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout containerLinearLayout;
        private LinearLayout bubbleLinearLayout;

        private TextView textViewName;
        private TextView textViewMessage;
        private TextView textViewDate;

        public void bind(final ChatMessage chatMessage) {
            textViewName.setText(chatMessage.getSenderName());
            textViewMessage.setText(chatMessage.getText());
            textViewDate.setText(/*TODO: format date */ "todo: parse me");

            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (chatMessage.isLocal()) {
                bubbleLinearLayout.setBackgroundResource(R.drawable.ic_outcoming);

                textViewName.setText(chatMessage.getSenderName());

                textViewName.setTextColor(context.getResources().getColor(R.color.primaryLight));
                textViewDate.setTextColor(context.getResources().getColor(R.color.primaryLight));
                textViewMessage.setTextColor(Color.BLACK);

                layoutParams.gravity = Gravity.END;
                textViewDate.setLayoutParams(layoutParams);

                containerLinearLayout.setGravity(Gravity.END);
            } else {
                bubbleLinearLayout.setBackgroundResource(R.drawable.ic_incoming);

                textViewName.setText(chatMessage.getSenderName());

                textViewName.setTextColor(context.getResources().getColor(R.color.primaryDark));
                textViewDate.setTextColor(context.getResources().getColor(R.color.primaryDark));
                textViewMessage.setTextColor(Color.WHITE);

                layoutParams.gravity = Gravity.START;
                textViewDate.setLayoutParams(layoutParams);

                containerLinearLayout.setGravity(Gravity.START);
            }
        }

        ViewHolder(final View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);

            containerLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutMessageContainer);
            bubbleLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutBubble);
        }
    }
}
