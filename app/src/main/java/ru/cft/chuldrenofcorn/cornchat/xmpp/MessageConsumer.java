package ru.cft.chuldrenofcorn.cornchat.xmpp;


/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 18:34
 */
public interface MessageConsumer {
    void consume(final String payload);
}
