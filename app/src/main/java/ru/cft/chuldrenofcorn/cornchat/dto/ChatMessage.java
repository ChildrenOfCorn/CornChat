package ru.cft.chuldrenofcorn.cornchat.dto;

/**
 * Created by azhukov on 26/08/16.
 */
public class ChatMessage {

    private String senderName;
    private String text;
    private String date;

    public ChatMessage(final String senderName, final String text, final String date) {
        this.senderName = senderName;
        this.text = text;
        this.date = date;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return text;
    }
}
