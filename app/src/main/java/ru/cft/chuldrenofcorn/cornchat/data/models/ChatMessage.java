package ru.cft.chuldrenofcorn.cornchat.data.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * User: azhukov
 * Date: 26.08.2016
 * Time: 14:35
 */
@DatabaseTable(tableName = "chatMessages")
public class ChatMessage {

    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String SENDER_NAME = "senderName";
    public static final String TEXT = "text";
    public static final String DATE = "date";
    public static final String IS_LOCAL = "isLocal";

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER, columnName = ID, id = true)
    @Getter
    @Setter
    private long id;

    @DatabaseField(dataType = DataType.STRING, columnName = SENDER_NAME)
    @Getter
    @Setter
    private String senderName;

    @DatabaseField(dataType = DataType.STRING, columnName = USER_ID)
    @Getter
    @Setter
    private String getUserId;


    @DatabaseField(dataType = DataType.STRING, columnName = TEXT)
    @Getter
    @Setter
    private String text;

    @DatabaseField(dataType = DataType.DATE, columnName = DATE)
    @Getter
    @Setter
    private Date date;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_LOCAL)
    @Getter
    @Setter
    private boolean isLocal; //если true - сообщение написал пользователь, если false - пришло из вне

    public ChatMessage(final String senderName, final String text, final Date date, boolean isLocal) {
        this.senderName = senderName;
        this.text = text;
        this.date = date;
        this.isLocal = isLocal;
    }

    @Override
    public String toString() {
        return text;
    }
}
