package ru.cft.chuldrenofcorn.cornchat.data.models;

import android.renderscript.Element;

import java.util.Date;

/**
 * Created by azhukov on 26/08/16.
 */
@DatabaseTable(tableName = "chatMessages")
public class ChatMessage {

    public static final String ID = "id";
    public static final String DATE = "date";
    @DatabaseField(canBeNull = false, dataType = Element.DataType.INTEGER, columnName = ID, id = true)
    private int id;

    private String senderName;
    private String text;
    @DatabaseField(canBeNull = true, dataType = Element.DataType.DATE, columnName = DATE)
    private Date date;

    public ChatMessage(final String senderName, final String text, final Date date) {
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
