package ru.cft.chuldrenofcorn.cornchat.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.cft.chuldrenofcorn.cornchat.R;
import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.presenter.ChatPresenter;

public class MainActivity extends AppCompatActivity {

    private final ChatPresenter presenter = new ChatPresenter(this);

    private ChatAdapter messageAdapter;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        recyclerViewMessages.setAdapter(presenter.getChatAdapter());

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!editTextMessage.getText().toString().isEmpty()) {
                    presenter.sendMessage(editTextMessage.getText().toString());
                }
            }
        });
    }
}
