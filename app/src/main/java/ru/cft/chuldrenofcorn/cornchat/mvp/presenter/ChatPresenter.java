package ru.cft.chuldrenofcorn.cornchat.mvp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;

import java.sql.SQLException;

import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageDao;
import ru.cft.chuldrenofcorn.cornchat.data.db.DatabaseHelper;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;
import ru.cft.chuldrenofcorn.cornchat.mvp.view.ChatView;
import ru.cft.chuldrenofcorn.cornchat.xmpp.ChatService;
import ru.cft.chuldrenofcorn.cornchat.xmpp.LocalBinder;

/**
 * Created by azhukov on 26/08/16.
 */
public class ChatPresenter extends MvpPresenter<ChatView> {

    private static final String TAG = ChatPresenter.class.getSimpleName();
    private ChatMessageDao dao;

    private ChatAdapter chatAdapter;
    private ChatService service;
    private boolean isBounded;

    public ChatPresenter(final Context context) {
        Log.d(TAG, "ChatPresenter: ");
        //Инициализация БД, TODO: вынести в даггер
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        dao = null;
        try {
            dao = new ChatMessageDao(databaseHelper.getDao(ChatMessage.class));
        } catch (SQLException e) {
            Log.e(TAG, "provideNewsItemsDao: ", e);
        }
        connectToService(context);
        doBindService(context);
    }

    private void connectToService(Context context) {
        Intent intent = new Intent(context, ChatService.class);
        context.startService(intent);
    }

    void doBindService(Context context) {
        context.bindService(new Intent(context, ChatService.class), connection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService(Context context) {
        if (connection != null) {
            context.unbindService(connection);
        }
    }

    private final ServiceConnection connection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            ChatPresenter.this.service = ((LocalBinder<ChatService>) service).getService();
            isBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            service = null;
            isBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    public void sendMessage(final String messageText) {
        //messages.add(new ChatMessage("vasya", messageText, new Date(), true));
    }
}
