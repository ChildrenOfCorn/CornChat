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
import lombok.NonNull;
import ru.cft.chuldrenofcorn.cornchat.common.Config;

public class ChatService extends Service {

	private static final String DOMAIN = "172.29.62.65";

	private static final String PASSWORD = "1";
	private static final int PORT = 5222;
	private static ConnectivityManager cm;
	private static XmppManager xmpp;
	private String text = "";

	private static final String TAG = ChatService.class.getSimpleName();

	@Override
	public IBinder onBind(final Intent intent) {
		return new LocalBinder<>(this);
	}

	public void connect(final MessageConsumer consumer, final String userName) {
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		xmpp = new XmppManager(ChatService.this, DOMAIN, PORT, Config.SERVER_HOST, userName, PASSWORD, Config.OPERATOR_ID, consumer);
		xmpp.connect("onCreate");
	}

	public void sendMessage(@NonNull final String payload) {
		xmpp.sendMessage(payload);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
							  final int startId) {
		Log.d(TAG, "onStartCommand: ");
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
