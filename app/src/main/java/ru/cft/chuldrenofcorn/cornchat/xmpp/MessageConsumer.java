package ru.cft.chuldrenofcorn.cornchat.xmpp;

import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 18:34
 */
public interface MessageConsumer {
    void consume(final ChatMessage message);
}