package ru.cft.chuldrenofcorn.cornchat.mvp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;

import ru.cft.chuldrenofcorn.cornchat.App;
import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.common.Config;
import ru.cft.chuldrenofcorn.cornchat.common.MockObjectBuilder;
import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageDao;
import ru.cft.chuldrenofcorn.cornchat.data.db.DatabaseHelper;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;
import ru.cft.chuldrenofcorn.cornchat.mvp.common.RxUtils;
import ru.cft.chuldrenofcorn.cornchat.mvp.view.ChatView;
import ru.cft.chuldrenofcorn.cornchat.xmpp.ChatService;
import ru.cft.chuldrenofcorn.cornchat.xmpp.LocalBinder;
import ru.cft.chuldrenofcorn.cornchat.xmpp.MessageConsumer;
import rx.Observable;

import java.sql.SQLException;
import java.util.Date;

/**
 * User: azhukov
 * Date: 26.08.2016
 * Time: 23:11
 */
@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements MessageConsumer {

    private static final String TAG = ChatPresenter.class.getSimpleName();
    private ChatMessageDao dao;

    private ChatAdapter chatAdapter;
    private ChatService service;
    private Context context = App.getAppContext();
    private boolean isBounded;

    private static final Gson gson = new Gson();

    public ChatPresenter() {
        Log.d(TAG, "ChatPresenter: ");
        //Инициализация БД, TODO: вынести в даггер
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        dao = null;
        try {
            dao = new ChatMessageDao((Dao<ChatMessage, Integer>) databaseHelper.getDao(ChatMessage.class));
        } catch (final SQLException e) {
            Log.e(TAG, "provideNewsItemsDao: ", e);
        }
        connectToService();
        doBindService();
    }

    private void connectToService() {
        context.startService(getIntent());
    }

    void doBindService() {
        context.bindService(getIntent(), connection,
                Context.BIND_AUTO_CREATE);
    }

    @NonNull
    private Intent getIntent() {
        return new Intent(context, ChatService.class);
    }

    void doUnbindService() {
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
            onBound();
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            service = null;
            isBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    private void onBound() {
        service.connect(this, getLocalId());
    }

    /**
     * Сохранить сообщение в бд и отправить запрос на сервер
     *
     * @param messageText
     */
    public void sendMessage(final String messageText) {
        // создаем объект Observable
        final Observable<String> observable = RxUtils.wrapMessage(messageText);
        // логика в IO потоке
        RxUtils.wrapAsync(observable)
                .flatMap(response -> {
                    // выполняется в IO потоке
                    if (isBounded) {
                        service.sendMessage(response);
                    }

                    ChatMessage chatMessage = ChatMessage.buildMessage(getLocalId(), null, messageText, new Date());
                    // добавить новое сообщение в бд
                    dao.add(chatMessage, getLocalId());
                    // вернуть выборку из бд с новым сообщением
                    return Observable.just(dao.getMessagesByUserId(getLocalId()));
                })
                // в UI потоке
                .subscribe(messageList -> {
                            Log.d(TAG, "sendMessage: received data");
                            getViewState().onDataReady(messageList);
                        }, exception -> {
                            Log.e(TAG, "sendMessage: except", exception);
                            //getViewState().onFail(exception.getMessage());
                        }

                );
    }

    /**
     * Сохранить сообщение в бд и дернуть метод View чтоб сообщить о новых сообщениях
     *
     * @param message
     */
    @Override
    public void consume(@lombok.NonNull final String payload) {
        //TODO: remove


        // создаем объект Observable
        final Observable<String> observable = RxUtils.wrapMessage(payload);
        // логика в IO потоке
        RxUtils.wrapAsync(observable)
                .flatMap(response -> {
                    final ChatMessage message = MockObjectBuilder.wrap(payload);
//                    final ChatMessage message = gson.fromJson(payload, ChatMessage.class);
                    if (message == null) {
                        //FIXME
                        //throw new Exception("Failed to parse JSON object");
                        return null;
                    }

                    if (message.isServiceMessage()) {
                        //TODO: Handle service message
                        return null;
                    }

                    dao.add(message, getLocalId());
                    // вернуть выборку из бд с новым сообщением
                    return Observable.just(dao.getMessagesByUserId(getLocalId()));
                })
                // в UI потоке
                .subscribe(messageList -> {
                            if (messageList == null) {
                                return;
                            }
                            Log.d(TAG, "sendMessage: received data");
                            getViewState().onDataReady(messageList);
                        }, exception -> {
                            Log.e(TAG, "sendMessage: except", exception);
                            //getViewState().onFail(exception.getMessage());
                        }

                );
    }

    /**
     * Метод остановки сервиса
     */
    public void shutDown() {
        doUnbindService();
        context.stopService(getIntent());
    }

    /**
     * Вернет EAN текущего пользователя
     *
     * @return
     */
    private String getLocalId() {
        return Config.USER_EAN;
    }
}
