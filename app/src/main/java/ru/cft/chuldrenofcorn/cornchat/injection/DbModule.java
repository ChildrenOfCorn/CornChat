package ru.cft.chuldrenofcorn.cornchat.injection;

import android.util.Log;

import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageDao;
import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageRepository;
import ru.cft.chuldrenofcorn.cornchat.data.db.DatabaseHelper;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * Created by grishberg on 27.08.16.
 */
@Module
public class DbModule {
    private static final String TAG = DbModule.class.getSimpleName();
    private final DatabaseHelper databaseHelper;
    private final ChatMessageRepository repository;

    public DbModule(DatabaseHelper databaseHelper, ChatMessageRepository repository) {
        this.databaseHelper = databaseHelper;
        this.repository = repository;
    }

    @Provides
    @Singleton
    public ChatMessageDao provideChatMessagesDao() {
        try {
            return new ChatMessageDao(databaseHelper.getDao(ChatMessage.class));
        } catch (SQLException e) {
            Log.e(TAG, "provideChatMessagesDao: ", e);
        }
        return null;
    }

    @Provides
    @Singleton
    public ChatMessageRepository provideChatMessageRepository() {
        return repository;
    }
}
