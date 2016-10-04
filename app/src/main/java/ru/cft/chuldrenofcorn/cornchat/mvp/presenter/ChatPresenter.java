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
import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.common.Config;
import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageRepository;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;
import ru.cft.chuldrenofcorn.cornchat.mvp.common.RxUtils;
import ru.cft.chuldrenofcorn.cornchat.mvp.view.ChatView;
import ru.cft.chuldrenofcorn.cornchat.ui.activity.MainActivity;
import ru.cft.chuldrenofcorn.cornchat.xmpp.ChatService;
import ru.cft.chuldrenofcorn.cornchat.xmpp.LocalBinder;
import ru.cft.chuldrenofcorn.cornchat.xmpp.MessageConsumer;
import rx.Observable;

import java.util.Date;

import javax.inject.Inject;

/**
 * User: azhukov
 * Date: 26.08.2016
 * Time: 23:11
 */
@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements MessageConsumer {

    private static final String TAG = ChatPresenter.class.getSimpleName();

    private ChatAdapter chatAdapter;
    private ChatService service;

    @Inject
    Context context;
    @Inject
    ChatMessageRepository repository;

    private boolean isBounded;

    public ChatPresenter() {
        Log.d(TAG, "ChatPresenter: ");
        MainActivity.getAppComponent().inject(this);
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
                    ChatMessage chatMessage = ChatMessage.buildMessage(getLocalId(), null, messageText, new Date());
                    if (isBounded) {
//                        service.sendMessage(response);
                        service.sendMessage(Config.gson.toJson(chatMessage));
                    }

                    // добавить новое сообщение в бд
                    repository.add(chatMessage, getLocalId());
                    // вернуть выборку из бд с новым сообщением
                    return Observable.just(repository.getMessages(getLocalId()));
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
     */
    @Override
    public void consume(@lombok.NonNull final String payload) {
        //TODO: remove

        // создаем объект Observable
        final Observable<String> observable = RxUtils.wrapMessage(payload);
        // логика в IO потоке
        RxUtils.wrapAsync(observable)
                .flatMap(response -> {
//                    final ChatMessage message = MockObjectBuilder.wrap(payload);
                    final ChatMessage message = Config.gson.fromJson(payload, ChatMessage.class);
                    if (message == null) {
                        //FIXME
                        //throw new Exception("Failed to parse JSON object");
                        return null;
                    }

                    if (message.isServiceMessage()) {
                        //TODO: Handle service message
                        return null;
                    }

                    repository.add(message, getLocalId());
                    // вернуть выборку из бд с новым сообщением
                    return Observable.just(repository.getMessages(getLocalId()));
                })
                // в UI потоке
                .subscribe(messageList -> {
                            if (messageList == null) {
                                return;
                            }
                            Log.d(TAG, "sendMessage: received data: "+messageList.size());
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
