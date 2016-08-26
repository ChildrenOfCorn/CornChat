package ru.cft.chuldrenofcorn.cornchat.presenter;

import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 20:27
 */
public interface ChatMessageListener {

	void onNewMessage(final ChatMessage internalMessage);
}
