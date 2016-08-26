package ru.cft.chuldrenofcorn.cornchat.mvp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;

import org.jivesoftware.smack.packet.Message;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageDao;
import ru.cft.chuldrenofcorn.cornchat.data.db.DatabaseHelper;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;
import ru.cft.chuldrenofcorn.cornchat.mvp.common.RxUtils;
import ru.cft.chuldrenofcorn.cornchat.mvp.view.ChatView;
import ru.cft.chuldrenofcorn.cornchat.xmpp.ChatService;
import ru.cft.chuldrenofcorn.cornchat.xmpp.LocalBinder;
import ru.cft.chuldrenofcorn.cornchat.xmpp.MessageConsumer;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by azhukov on 26/08/16.
 */
public class ChatPresenter extends MvpPresenter<ChatView> implements MessageConsumer {

    private static final String TAG = ChatPresenter.class.getSimpleName();
    private ChatMessageDao dao;

    private ChatAdapter chatAdapter;
    private ChatService service;
    private Context context;
    private boolean isBounded;

    public ChatPresenter(final Context context) {
        Log.d(TAG, "ChatPresenter: ");
        this.context = context;
        //Инициализация БД, TODO: вынести в даггер
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        dao = null;
        try {
            dao = new ChatMessageDao(databaseHelper.getDao(ChatMessage.class));
        } catch (SQLException e) {
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
        service.connect(this/* ,login*/);
    }

    /**
     * Сохранить сообщение в бд и отправить запрос на сервер
     *
     * @param messageText
     */
    public void sendMessage(final String messageText) {
        // создаем объект Observable
        Observable<String> observable = RxUtils.wrapMessage(messageText);
        // логика в IO потоке
        RxUtils.wrapAsync(observable)
                .flatMap(response -> {
                    // выполняется в IO потоке
                    if (isBounded) {
                        //service.sendMessage(response, userId)
                    }
                    // добавить новое сообщение в бд
                    ChatMessage chatMessage = new ChatMessage(
                            "",
                            response,
                            new Date(), true);
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
    public void consume(Message message) {
        //TODO: remove
        String rawText = message.getBody();

        // создаем объект Observable
        Observable<String> observable = RxUtils.wrapMessage(rawText);
        // логика в IO потоке
        RxUtils.wrapAsync(observable)
                .flatMap(response -> {
                    // выполняется в IO потоке
                    //TODO: дессериализовать в ChatMessage из
                    // добавить новое сообщение в бд
                    ChatMessage chatMessage = new ChatMessage(
                            "",
                            response,
                            new Date(),
                            false);
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
        return "";
    }
}
