package ru.cft.chuldrenofcorn.cornchat.xmpp;

/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 14:38
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import ru.cft.chuldrenofcorn.cornchat.dto.ChatMessage;

public class ChatService extends Service {
	private static final String DOMAIN = "172.29.62.65";
	private static final String USERNAME = "2960291738335";
	private static final String PASSWORD = "1";
	private static final int PORT = 5222;
	private static ConnectivityManager cm;
	private static XmppManager xmpp;
	private String text = "";

	private static final String TAG = "cornchat.xmpp.ChatService";

	private final MessageConsumer consumerStub = new MessageConsumer() {

		@Override
		public void consume(final ChatMessage message) {
			Log.e(TAG, "New message: "+message.getText());
		}
	};

	@Override
	public IBinder onBind(final Intent intent) {
		return new LocalBinder<ChatService>(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		xmpp = XmppManager.getInstance(ChatService.this, DOMAIN, PORT, USERNAME, PASSWORD, consumerStub);
		xmpp.connect("onCreate");
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
							  final int startId) {
		return Service.START_NOT_STICKY;
	}

	@Override
	public boolean onUnbind(final Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		xmpp.disconnect();
	}

	public static boolean isNetworkConnected() {
		return cm.getActiveNetworkInfo() != null;
	}
}
