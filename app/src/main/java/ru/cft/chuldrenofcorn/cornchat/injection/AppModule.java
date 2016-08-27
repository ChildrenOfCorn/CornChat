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
    private Context context;
    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

}
