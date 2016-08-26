package ru.cft.chuldrenofcorn.cornchat.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import ru.cft.chuldrenofcorn.cornchat.R;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<ChatMessage> messageAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAdapter();
        initViews();
    }

    private void initAdapter() {
        messageAdapter = new ArrayAdapter<ChatMessage>(getApplicationContext(), R.layout.list_item_chat, R.id.textViewName);
    }

    private void initViews() {
        final ImageButton imageButtonSend = (ImageButton) findViewById(R.id.imageButtonSend);
        final EditText editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        final ListView listView = (ListView) findViewById(R.id.listViewMessages);

        listView.setAdapter(messageAdapter);

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!editTextMessage.getText().toString().isEmpty()) {
                    messageAdapter.add(new ChatMessage("test", editTextMessage.getText().toString(), "test"));
                }
            }
        });
    }
}
