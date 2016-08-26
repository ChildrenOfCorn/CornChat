package ru.cft.chuldrenofcorn.cornchat.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.cft.chuldrenofcorn.cornchat.R;
import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.presenter.ChatPresenter;
import ru.cft.chuldrenofcorn.cornchat.xmpp.ChatService;
import ru.cft.chuldrenofcorn.cornchat.xmpp.LocalBinder;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	private final ChatPresenter presenter = new ChatPresenter(this);
	private ChatAdapter messageAdapter;
	private boolean mBounded;
	private ChatService mService;

	private final ServiceConnection mConnection = new ServiceConnection() {

		@SuppressWarnings("unchecked")
		@Override
		public void onServiceConnected(final ComponentName name,
									   final IBinder service) {
			mService = ((LocalBinder<ChatService>) service).getService();
			mBounded = true;
			Log.d(TAG, "onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(final ComponentName name) {
			mService = null;
			mBounded = false;
			Log.d(TAG, "onServiceDisconnected");
		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		doBindService();
		initViews();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		doUnbindService();
	}

	void doBindService() {
		bindService(new Intent(this, ChatService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	void doUnbindService() {
		if (mConnection != null) {
			unbindService(mConnection);
		}
	}


	private void initViews() {
		final ImageButton imageButtonSend = (ImageButton) findViewById(R.id.imageButtonSend);
		final EditText editTextMessage = (EditText) findViewById(R.id.editTextMessage);
		final RecyclerView recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);

		final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
		recyclerViewMessages.setLayoutManager(linearLayoutManager);
		recyclerViewMessages.setAdapter(presenter.getChatAdapter());

        recyclerViewMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged()");
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!editTextMessage.getText().toString().isEmpty()) {
                    presenter.sendMessage(editTextMessage.getText().toString());
                    editTextMessage.setText("");
                }
            }
        });
    }
}
