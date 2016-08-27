package ru.cft.chuldrenofcorn.cornchat;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageRepositoryArray;
import ru.cft.chuldrenofcorn.cornchat.data.db.DatabaseHelper;
import ru.cft.chuldrenofcorn.cornchat.injection.AppComponent;
import ru.cft.chuldrenofcorn.cornchat.injection.AppModule;
import ru.cft.chuldrenofcorn.cornchat.injection.DaggerAppComponent;
import ru.cft.chuldrenofcorn.cornchat.injection.DbModule;
import ru.cft.chuldrenofcorn.cornchat.mvp.presenter.ChatPresenter;

/**
 * Created by grishberg on 26.08.16.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    private static AppComponent sAppComponent;
    private static App sInstanse;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstanse = this;
        //sAppComponent = Da;
    }

    public synchronized static AppComponent getAppComponent() {
        if (sAppComponent == null) {
            initComponents(DaggerAppComponent
                    .builder()
                    .dbModule(new DbModule(
                            new DatabaseHelper(sInstanse.getApplicationContext()),
                            new ChatMessageRepositoryArray()))
                    .appModule(new AppModule(sInstanse))
                    .build()
            );
        }
        return sAppComponent;
    }

    /**
     * Если будут тесты, то замокенный AppComponent передается тут
     *
     * @param component
     */
    public static void initComponents(AppComponent component) {
        sAppComponent = component;
    }
}
