package ru.cft.chuldrenofcorn.cornchat.xmpp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NonNull;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import java.io.IOException;
import java.util.Date;

/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 14:35
 */
public class XmppManager {

    private static final String TAG = XmppManager.class.getSimpleName();
    private static boolean connected = false;
    private boolean loggedin = false;
    private boolean isConnecting = false;
    private XMPPTCPConnection connection;
    private final String loginUser;
    private final String loginPassword;
    private final String operatorId;
    private final String serverAddress;
    private final String serverHost;
    private ChatService context;
    private static XmppManager instance = null;
    private Chat chat;
    private MessageConsumer messageConsumer;

    public XmppManager(final ChatService context,
                       final String serverAdress,
                       final int serverPort,
                       final String serverHost,
                       final String loginUser,
                       final String loginPassword,
                       final String operatorId,
                       final MessageConsumer consumer) {

        this.loginUser = loginUser;
        this.loginPassword = loginPassword;
        this.serverAddress = serverAdress;
        this.messageConsumer = consumer;
        this.operatorId = operatorId;
        this.serverHost = serverHost;
        mMessageListener = new MMessageListener(context);
        mChatManagerListener = new ChatManagerListenerImpl();
        initialiseConnection(serverAdress, serverPort);
    }

    private ChatManagerListenerImpl mChatManagerListener;
    private MMessageListener mMessageListener;

    private String text = "";
    private String mMessage = "", mReceiver = "";

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (final ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    private void initialiseConnection(final String serverAddress, final int serverPort) {

        final XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(serverAddress);
        config.setHost(serverAddress);
        config.setPort(serverPort);
        config.setDebuggerEnabled(true);
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);

        connection = new XMPPTCPConnection(config.build());

        final XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public void connect(final String caller) {

        final AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final Void... arg0) {
                if (connection.isConnected()) {
                    return false;
                }
                isConnecting = true;
                Log.d("Connect() Function", caller + "=>connecting....");

                try {
                    connection.connect();
                    final DeliveryReceiptManager dm = DeliveryReceiptManager
                            .getInstanceFor(connection);
                    dm.setAutoReceiptMode(AutoReceiptMode.always);
                    dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                        @Override
                        public void onReceiptReceived(final String fromid,
                                                      final String toid, final String msgid,
                                                      final Stanza packet) {

                        }
                    });
                    connected = true;
                } catch (final IOException e) {
                    Log.e("(" + caller + ")", "IOException: " + e.getMessage());
                } catch (final SmackException e) {
                    Log.e("(" + caller + ")",
                            "SMACKException: " + e.getMessage());
                } catch (final XMPPException e) {
                    Log.e("connect(" + caller + ")",
                            "XMPPException: " + e.getMessage());
                }

                isConnecting = false;
                return false;
            }
        };
        connectionThread.execute();
    }

    public void login() {

        try {
            SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            connection.login(loginUser, loginPassword);
            Log.i("LOGIN", "Yey! We're connected to the Xmpp server!");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private class ChatManagerListenerImpl implements ChatManagerListener {

        @Override
        public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                                final boolean createdLocally) {
            if (!createdLocally) {
                chat.addMessageListener(mMessageListener);
            }
        }
    }

    public void sendMessage(@NonNull final String payload) {

        if (chat == null) {
            chat = ChatManager.getInstanceFor(connection).createChat(
                     operatorId + "@" + serverHost,
                    mMessageListener);

        }

        final Message message = new Message();
        message.setBody(payload);
        //TODO: use real id
        message.setStanzaId(new Date().toString());
        message.setType(Message.Type.chat);

        try {
            if (connection.isAuthenticated()) {
                chat.sendMessage(message);
            } else {
                login();
            }
        } catch (final SmackException.NotConnectedException exception) {
            //TODO: Handle me
            Log.e(TAG, "msg Not sent!-Not Connected!", exception);
        } catch (final Exception exception) {
            Log.wtf(TAG, "xmpp.SendMessage()-Exception", exception);
        }
    }

    public class XMPPConnectionListener implements ConnectionListener {

        @Override
        public void connected(final XMPPConnection connection) {

            Log.d("xmpp", "Connected!");
            connected = true;
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            Log.d("xmpp", "ConnectionCLosed!");
            connected = false;
            chat = null;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(final Exception arg0) {
            Log.d("xmpp", "ConnectionClosedOn Error!");
            connected = false;

            chat = null;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(final int arg0) {

            Log.d("xmpp", "Reconnectingin " + arg0);

            loggedin = false;
        }

        @Override
        public void reconnectionFailed(final Exception arg0) {

            Log.d("xmpp", "ReconnectionFailed!");
            connected = false;

            chat = null;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            Log.d("xmpp", "ReconnectionSuccessful");
            connected = true;

            chat = null;
            loggedin = false;
        }

        @Override
        public void authenticated(final XMPPConnection arg0, final boolean arg1) {
            Log.d("xmpp", "Authenticated!");
            loggedin = true;

            ChatManager.getInstanceFor(connection).addChatListener(mChatManagerListener);
            chat = null;
        }
    }

    private class MMessageListener implements ChatMessageListener {

        MMessageListener(final Context context) {

        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                    + message);
            XmppManager.this.chat = chat;

            if (message.getType() == Message.Type.chat
                    && message.getBody() != null) {
                messageConsumer.consume(message.getBody());
            }
        }
    }
}
