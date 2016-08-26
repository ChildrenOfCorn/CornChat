package ru.cft.chuldrenofcorn.cornchat.data.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User: azhukov
 * Date: 26.08.2016
 * Time: 14:35
 */
@ToString
@DatabaseTable(tableName = "chatMessages")
public class ChatMessage {

    public static final String ID = "id";
    public static final String SENDER_ID = "senderId";
    public static final String RECEIVER_ID = "receiverId";
    public static final String SERVICE_NAME = "serviceName";
    public static final String PAYLOAD = "text";
    public static final String DATE = "date";

    private static final int PAYLOAD_TYPE_MESSAGE = 100;
    private static final int PAYLOAD_TYPE_SERVICE = 101;

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER, columnName = ID, id = true)
    @Getter
    private int Id;

    @Getter
    private int messageType;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = SERVICE_NAME)
    @Getter
    private String serviceUserName;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = SENDER_ID)
    @Getter
    private String senderId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = RECEIVER_ID)
    @Getter
    private String receiverId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = PAYLOAD)
    @Getter
    private String payload;

    @DatabaseField(canBeNull = false, dataType = DataType.DATE, columnName = DATE)
    @Getter
    private Date date;

    public ChatMessage() {
    }

    private ChatMessage(final String senderId, final String receiverId, final String payload, final Date date, final int payloadType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.payload = payload;
        this.date = date;
        this.messageType = payloadType;
    }
    public static ChatMessage buildMessage(final String senderId, final String receiverId, final String payload, final Date date) {
        return new ChatMessage(senderId, receiverId, payload, date, PAYLOAD_TYPE_MESSAGE);
    }
}
