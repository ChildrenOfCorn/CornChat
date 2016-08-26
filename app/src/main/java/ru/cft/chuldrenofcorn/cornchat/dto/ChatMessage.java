package ru.cft.chuldrenofcorn.cornchat.dto;

/**
 * Created by azhukov on 26/08/16.
 */
public class ChatMessage {

    private String senderName;
    private String text;
    private String date;
    private boolean isLocal; //если true - сообщение написал пользователь, если false - пришло из вне

    public ChatMessage(final String senderName, final String text, final String date, final boolean isLocal) {
        this.senderName = senderName;
        this.text = text;
        this.date = date;
        this.isLocal = isLocal;
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

    public boolean isLocal() {
        return isLocal;
    }
}
