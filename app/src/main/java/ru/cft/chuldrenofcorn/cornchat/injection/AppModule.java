package ru.cft.chuldrenofcorn.cornchat.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.cft.chuldrenofcorn.cornchat.App;

/**
 * Created by grishberg on 27.08.16.
 */
@Module
public class AppModule {
    private static final String TAG = AppModule.class.getSimpleName();
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app.getApplicationContext();
    }

}
