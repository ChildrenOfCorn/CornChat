package ru.cft.chuldrenofcorn.cornchat.mvp.view;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * Created by azhukov on 26/08/16.
 */
public interface ChatView extends MvpView{

    void onDataReady(List<ChatMessage> messages);

}
