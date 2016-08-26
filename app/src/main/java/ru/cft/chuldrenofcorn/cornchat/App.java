package ru.cft.chuldrenofcorn.cornchat;

import android.app.Application;
import android.content.Context;

import ru.cft.chuldrenofcorn.cornchat.mvp.presenter.ChatPresenter;

/**
 * Created by grishberg on 26.08.16.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    private static ChatPresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        presenter = new ChatPresenter(getApplicationContext());
    }
}
