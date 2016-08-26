package ru.cft.chuldrenofcorn.cornchat.common;

import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

import java.util.Date;

/**
 * User: asmoljak
 * Date: 27.08.2016
 * Time: 0:32
 */
//TODO: delete me
public class MockObjectBuilder {
	public static ChatMessage wrap(final String message) {
		final ChatMessage wrappedMessage = ChatMessage.buildMessage("SomeUser", Config.USER_EAN, message, new Date());
		wrappedMessage.setServiceUserName("Anastasiya Vasilkova");

		return wrappedMessage;
	}
}
