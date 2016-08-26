package ru.cft.chuldrenofcorn.cornchat.xmpp;

import org.jivesoftware.smack.packet.Message;

/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 18:34
 */
public interface MessageConsumer {
    void consume(final Message message);
}
