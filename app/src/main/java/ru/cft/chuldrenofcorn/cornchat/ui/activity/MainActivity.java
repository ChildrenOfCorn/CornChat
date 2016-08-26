package ru.cft.chuldrenofcorn.cornchat.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.cft.chuldrenofcorn.cornchat.R;
import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;
import ru.cft.chuldrenofcorn.cornchat.mvp.presenter.ChatPresenter;
import ru.cft.chuldrenofcorn.cornchat.mvp.view.ChatView;
import ru.cft.chuldrenofcorn.cornchat.ui.common.BaseActivity;

import java.util.List;

public class MainActivity extends BaseActivity implements ChatView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectPresenter
    ChatPresenter presenter;

    private ChatAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        final ImageButton imageButtonSend = (ImageButton) findViewById(R.id.imageButtonSend);
        final EditText editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        final RecyclerView recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);

        adapter = new ChatAdapter(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        recyclerViewMessages.setAdapter(adapter);

        recyclerViewMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged()");
                final View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

    @Override
    public void onDataReady(final List<ChatMessage> messages) {
        adapter.setMessages(messages);
    }
}
