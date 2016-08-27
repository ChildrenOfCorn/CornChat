package ru.cft.chuldrenofcorn.cornchat.injection;

import javax.inject.Singleton;

import dagger.Component;
import ru.cft.chuldrenofcorn.cornchat.data.db.ChatMessageRepositoryDb;
import ru.cft.chuldrenofcorn.cornchat.mvp.presenter.ChatPresenter;

/**
 * Created by grishberg on 27.08.16.
 */
@Singleton
@Component(modules = {AppModule.class, DbModule.class})
public interface AppComponent {

    void inject(ChatPresenter chatPresenter);

    void inject(ChatMessageRepositoryDb chatMessageRepositoryDb);
}
