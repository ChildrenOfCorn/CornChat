package ru.cft.chuldrenofcorn.cornchat.data.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * Created by grishberg on 26.08.16.
 * Класс для работы с бд
 */
public class ChatMessageDao {
    private static final String TAG = ChatMessageDao.class.getSimpleName();

    private final Dao<ChatMessage, Integer> dao;

    public ChatMessageDao(Dao<ChatMessage, Integer> dao) {
        this.dao = dao;
    }

    public boolean add(ChatMessage message, String userId) {
        try {
            message.setUserId(userId);
            dao.createOrUpdate(message);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "add: ", e);
        }
        return false;
    }

    /**
     * Добавить в бд новые элементы для списка покупок
     *
     * @return
     */
    public boolean add(List<ChatMessage> messages, String userId) {
        try {
            // удалить старые элементы для данного листа
            deleteByUserId(userId);
            for (int i = 0; i < messages.size(); i++) {
                ChatMessage message = messages.get(i);
                message.setUserId(userId);
                dao.createOrUpdate(message);
            }
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "add: ", e);
        }
        return false;
    }

    public boolean deleteByUserId(String userId) {
        try {
            DeleteBuilder<ChatMessage, Integer> deleteBuilder =
                    dao.deleteBuilder();
            deleteBuilder.where().eq(ChatMessage.USER_ID, userId);
            deleteBuilder.delete();
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "deleteByUserId: ", e);
        }
        return false;
    }

    /**
     * Удалить все записи из бд
     *
     * @return
     */
    public boolean deleteAll() {
        try {
            DeleteBuilder<ChatMessage, Integer> deleteBuilder =
                    dao.deleteBuilder();
            deleteBuilder.delete();
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "deleteAll: ", e);
        }
        return false;
    }

    public void delete(int messageId) {
        try {
            dao.deleteById(messageId);
        } catch (SQLException e) {
            Log.e(TAG, "delete: ", e);
        }
    }

    public List<ChatMessage> getMessagesByUserId(final String userId) {
        List<ChatMessage> list = null;

        try {
            final QueryBuilder<ChatMessage, Integer> queryBuilder = dao.queryBuilder()
                    .orderBy(ChatMessage.DATE, false);

            final Where where = queryBuilder.where();
            where.eq(ChatMessage.USER_ID, userId);

            final PreparedQuery<ChatMessage> preparedQuery = queryBuilder.prepare();
            list = dao.query(preparedQuery);
        } catch (SQLException e) {
            Log.e(TAG, "getMessagesByUserId: ", e);
        }
        return list;
    }
}

