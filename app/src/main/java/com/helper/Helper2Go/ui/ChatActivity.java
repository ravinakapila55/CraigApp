package com.helper.Helper2Go.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.ListType;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.ChatListAdapter;
import com.helper.Helper2Go.models.MessageData;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity
{
    public static ChatActivity activity;

    private static final String TAG = ChatActivity.class.getSimpleName();
    Socket mSocket;

    String user_name;

    String conversationId;

    String senderId;
    String receiverId;
    String receiver_job_id;
    String sender_job_id;
    String noti_type;
    String budgetValue;
    String offerValue;
    String other_infoValue;

    @BindView(R.id.edt_message)
    EditText edt_message;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.offer)
    TextView offer;

    @BindView(R.id.other_info)
    TextView other_info;

    @BindView(R.id.budget)
    TextView budget;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    int PAGE_SIZE = 0;
    List<String> stringList = new ArrayList<>();

    /* private Socket mSocket;
    {
        try {
            Manager manager = new Manager(new URI("http://socket.com"));
            Socket socket = manager.socket("/" + doctor_id);
            socket.connect();
            //mSocket = IO.socket(ConfigConstants.SOCKET_URL);
        } catch (URISyntaxException e) {}
    }*/

    public int pageCount = 1;
    public int totalPages = 1;
    View footerview;
    public ProgressBar loadmore_progressbar;
    LinearLayoutManager linearLayoutManager;


    private ProgressBar progressBar;

    private boolean isLoading = false;
    private boolean isLastPage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        activity = this;
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        linearLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(linearLayoutManager);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent intent = getIntent();
        if (intent != null) {

            if (getIntent().hasExtra("noti_type"))
            {
                noti_type=getIntent().getExtras().getString("noti_type");
                Log.e("noti_type ",noti_type);
                senderId = intent.getStringExtra("senderId");
                receiverId = intent.getStringExtra("receiverId");
                receiver_job_id = intent.getStringExtra("receiver_job_id");
                sender_job_id = intent.getStringExtra("sender_job_id");
                user_name="";
            }
            else {
                Log.e("noti_type ","nottt");
                senderId = intent.getStringExtra("senderId");
                receiverId = intent.getStringExtra("receiverId");
                receiver_job_id = intent.getStringExtra("receiver_job_id");
                sender_job_id = intent.getStringExtra("sender_job_id");
                user_name = intent.getStringExtra("receiver_name");
                budgetValue = intent.getStringExtra("budget");
                offerValue = intent.getStringExtra("offer");
                other_infoValue= intent.getStringExtra("other");

                offer.setText(offerValue);
                other_info.setText(other_infoValue);
                budget.setText("CHF "+budgetValue);
            }

        }

        /*
        senderId = "129";
        receiverId = "134";
        sender_job_id = "2"; // applicant id
        receiver_job_id = "9"; // job id
        user_name = "Inna Martin";
       */

        tvTitle.setText(user_name);

        setConnectionListner();

        //recycler.addOnScrollListener(new CustomScrollListener());

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (pastVisiblesItems == 0) {
                        isLoading = true;
                        progressBar.setVisibility(View.VISIBLE);
                        if (pageCount == totalPages + 1) {
                            progressBar.setVisibility(View.GONE);
                            isLoading = false;
                        } else {
                            if (isLoading) {
                                isLoading = false;
                                LoadMoreConerstation(pageCount);
                            }
                        }
                    }
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });
        getConversationIdApi(pageCount);
    }

    public static ChatActivity getInstance()
    {
        return activity;
    }

    private void resultAction(List<MessageData> model)
    {
        progressBar.setVisibility(View.INVISIBLE);
        isLoading = false;
        if (model != null)
        {
            chatListAdapter.addItems(model);
            if (pageCount == totalPages)
            {
                isLastPage = true;
            }
            /*else {
                pageCount = model.getPage() + 1;
            }*/
        }
    }

    List<MessageData> messageDataList = new ArrayList<>();
    static List<MessageData> tempMessageDataList = new ArrayList<>();
    ChatListAdapter chatListAdapter;

    private void LoadMoreConerstation(int pageCounts)
    {
        if (!NetworkUtils.isNetworkAvailable(ChatActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), ChatActivity.this);
            return;
        }

        TinderAppInterface apiInterface = Injector.provideChatApi();

        Observable<Response<ResponseBody>> api = apiInterface.getNotifications("Bearer " + MyApplication.getInstance().useString("user_access_token"));
         //http://178.128.116.149:4201/api/chat/conversation/id?sender=215&receiver=208&sender_job_id=78&receiver_job_id=88
         //atul=208
        Observable<Response<ResponseBody>> observeApi = api
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>()
        {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {

                    JSONObject jsonObject1 = generalResponse.response;

                    if (jsonObject1.getBoolean("status")) {

                        pageCount = Integer.parseInt(jsonObject1.getString("page_number")) + 1;
                        totalPages = jsonObject1.getInt("pages");

                        List<MessageData> newMessageDataList = new ArrayList<>();
                        JSONArray jsonArrayForMessage = jsonObject1.getJSONArray("messages");
                        if (jsonArrayForMessage.length() != 0) {

                            for (int i = 0; i < jsonArrayForMessage.length(); i++) {

                                JSONObject jsonObjectMessage = jsonArrayForMessage.getJSONObject(i);
                                String title = "";
                                int viewType = 0;
                                MessageData messageData = new MessageData();
                                if (jsonObjectMessage.getString("sender").equals(senderId)) {
                                    title = "You";
                                    viewType = ListType.SENDER_VIEW_TYPE;
                                } else {
                                    title = user_name;
                                    viewType = ListType.RECEIVER_VIEW_TYPE;
                                }

                                messageData.setId(jsonObjectMessage.getString("id"));
                                messageData.setConversationId(jsonObjectMessage.getString("conversationId"));
                                messageData.setMessage(jsonObjectMessage.getString("message"));
                                messageData.setSender(jsonObjectMessage.getString("sender"));
                                messageData.setCreatedAt(jsonObjectMessage.getString("createdAt"));
                                messageData.setType(viewType);
                                messageData.setNameTitle(title);

                                newMessageDataList.add(messageData);
                            }

                            tempMessageDataList.clear();
                            tempMessageDataList.addAll(messageDataList);
                            messageDataList.clear();
                            messageDataList.addAll(newMessageDataList);
                            messageDataList.addAll(tempMessageDataList);
                            chatListAdapter.addListToTop(messageDataList);
                            progressBar.setVisibility(View.GONE);

                            Log.e("Size...............", String.valueOf(messageDataList.size()));
                            chatListAdapter = new ChatListAdapter(messageDataList, ChatActivity.this);
                            recycler.setAdapter(chatListAdapter);

                        } else {

                        }

                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//mani-231 paras-230
                //http://178.128.116.149:4201/api/chat/conversation/id?sender=231&receiver=230&sender_job_id=88&receiver_job_id=99
                //http://178.128.116.149:4201/api/chat/conversation/id?sender=231&receiver=230&sender_job_id=99&receiver_job_id=88
            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(ChatActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
               // MyApplication.getInstance().hideProgress(ChatActivity.this);
            }

            @Override
            public void onComplete()
            {
               // MyApplication.getInstance().hideProgress(ChatActivity.this);
            }
        });
    }

    public void setConnectionListner()
    {
     try
     {
            Log.e(TAG, "connectSocket: " + "inside");
            Log.e(TAG, "Url: " + Injector.SOCKET_URL);

            //mSocket = IO.socket(ConfigConstants.SOCKET_URL + senderId);
            mSocket = IO.socket(Injector.SOCKET_URL);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onTimeConnectError);
            mSocket.on("message_received", onNewMessage);
            mSocket.connect();
           //emitRoomJoin();
     }

     catch (Exception e)
     {
          e.printStackTrace();
      }
    }

    private Emitter.Listener onConnect = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
            Log.e(TAG, "call: onConnect" + args.length);
            Log.e(TAG, "call: onConnect" + args.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    mSocket.emit("init", senderId);
                    Log.e(TAG, "onConnect");
                    for (int jj = 0; jj < args.length; jj++)
                    {
                        Log.e(TAG, "OnConnectResponseDriverHome " + args[jj]);
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
            Log.e(TAG, "call: onDisconnect" + args.length);
            Log.e(TAG, "call: onDisconnect" + args.toString());
            runOnUiThread(new Runnable()
            {

                @Override
                public void run()
                {
                    Log.e(TAG, "onDisconnect");
                    for (int jj = 0; jj < args.length; jj++)
                    {
                        Log.e(TAG, "onDisconnectResponseDriverHome " + args[jj]);
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "call: onConnectError" + args.length);
            Log.e(TAG, "call: onConnectError" + args.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "onConnectError");
                    for (int jj = 0; jj < args.length; jj++) {
                        Log.e(TAG, "onConnectErrorResponseDriverHome " + args[jj]);
                    }
                }
            });
        }
    };

    private Emitter.Listener onTimeConnectError = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "call: onTimeConnectError" + args.length);
            Log.e(TAG, "call: onTimeConnectError" + args.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "onTimeConnectError");
                    for (int jj = 0; jj < args.length; jj++) {
                        Log.e(TAG, "onTimeConnectErrorResponseDriverHome " + args[jj]);
                    }
                }
            });
        }
    };

    private void callSendNotification()
    {
        if (!NetworkUtils.isNetworkAvailable(ChatActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), ChatActivity.this);
            return;
        }

     //   MyApplication.getInstance().showProgress(ChatActivity.this, "", "");
        TinderAppInterface apiInterface = Injector.provideApi();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("sender_id", senderId);
            obj.addProperty("reciver_id", receiverId);
            obj.addProperty("sender_job_id", sender_job_id);
            obj.addProperty("receiver_job_id", receiver_job_id);
        /*    obj.addProperty("message", message);
            */
            Log.e("ParamsSendMessageApiii ",obj.toString());

            Observable<Response<ResponseBody>> observeApi = apiInterface.sendChatNotification(obj,
                    "Bearer " + MyApplication.getInstance().useString("user_access_token"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            observeApi.subscribe(new Observer<Response<ResponseBody>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Response<ResponseBody> response) {

                    Log.e("Data_Loading_Error", String.valueOf(response));
                    GeneralResponse generalResponse = new GeneralResponse(response);
                    Log.e("request....", String.valueOf(generalResponse.response));
                    try {
                        if (generalResponse.checkStatus()) {
                            MyApplication.getInstance().hideProgress(ChatActivity.this);

                        } else {
                            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                                    generalResponse.getMessage(), ChatActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Throwable e) {
                    MyApplication.showToast(ChatActivity.this, e.getMessage());
                    Log.i("Data_Loading_Error", e.toString());
                    MyApplication.getInstance().hideProgress(ChatActivity.this);
                }

                @Override
                public void onComplete() {
                 //   MyApplication.getInstance().hideProgress(ChatActivity.this);
                }
            });
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }



    }

    private void attemptSend()
    {
        final String message = edt_message.getText().toString().trim();
        if (TextUtils.isEmpty(message))
        {
            return;
        }

        JSONObject obj = new JSONObject();
        try
        {
            obj.put("sender", senderId);
            obj.put("receiver", receiverId);
            obj.put("message", message);
            obj.put("sender_job_id", sender_job_id);
            obj.put("receiver_job_id", receiver_job_id);
            Log.e("ParamsSendMessage ",obj.toString());
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        edt_message.setText("");
        // mSocket.emit("message_sent", senderId, String.valueOf(obj));



        mSocket.emit("message_sent", String.valueOf(obj), new Ack() {

            @Override
            public void call(Object... args) {
                Log.e("socketio", "message back: " + args);


            }
        });
        //addMessage(user_name, message);

        callSendNotification();//to send the notification to other user

//        callSendMessage(message);
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args) {

            Log.e(TAG, "call: onNewMessageError" + args.length);
            Log.e(TAG, "call: onNewMessageError" + args.toString());

            runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.e(TAG, String.valueOf(data));
                    String receiver_job_id;
                    String sender_job_id;
                    String message;
                    String sender;
                    String createdAt;
                    try {
                        receiver_job_id = data.getString("receiver_job_id");
                        sender_job_id = data.getString("sender_job_id");
                        message = data.getString("message");
                        sender = data.getString("sender");
                        createdAt = data.getString("time");
                        addMessage(user_name, message, sender, createdAt);
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    // addMessage(username, message);
                }
            });
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("message_received", onNewMessage);
        activity = null;

    }

    @OnClick(R.id.send)
    public void send()
    {
        attemptSend();
    }





    public void addMessage(String userName, String message, String senderId, String createdAt)
    {

        String title;
        int viewType;

        MessageData messageData = new MessageData();
        // if (senderId.equals(sharePref.getUserInfo().getId())) {
        if (senderId.equals(this.senderId))
        {
            title = "You";
            viewType = ListType.SENDER_VIEW_TYPE;
        }
        else
        {
            title = userName;
            viewType = ListType.RECEIVER_VIEW_TYPE;
        }

        messageData.setId("");
        messageData.setConversationId(conversationId);
        messageData.setMessage(message);
        messageData.setSender(senderId);
        messageData.setCreatedAt(createdAt);
        messageData.setType(viewType);
        messageData.setNameTitle(title);
        messageDataList.add(messageData);

        if (chatListAdapter == null)
        {
            chatListAdapter = new ChatListAdapter(messageDataList, ChatActivity.this);
            recycler.setAdapter(chatListAdapter);
            scrollToBottom();
        }
        else
        {
            chatListAdapter.notifyDataSetChanged();
        }
        scrollToBottom();
    }


    private void scrollToBottom()
    {
        recycler.scrollToPosition(chatListAdapter.getItemCount() - 1);
    }


    @OnClick(R.id.img_back)
    public void img_back()
    {
        onBackPressed();
//        finish();
    }

    private void getConversationIdApi(final int page)
    {
        if (!NetworkUtils.isNetworkAvailable(ChatActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), ChatActivity.this);
            return;
        }

        TinderAppInterface apiInterface = Injector.provideChatApi();
        MyApplication.getInstance().showProgress(ChatActivity.this, "", "");
        Observable<Response<ResponseBody>> api = apiInterface.getConversationId(senderId, receiverId, sender_job_id, receiver_job_id);

        Observable<Response<ResponseBody>> observeApi = api
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {

                Log.e("Data_Loading_ErrorGet", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {

                    JSONObject jsonObject1 = generalResponse.response;
                    if (jsonObject1.getBoolean("status")) {
                        MyApplication.getInstance().hideProgress(ChatActivity.this);
                        conversationId = jsonObject1.getString("conversationId");
                        Log.e("conversationId", String.valueOf(conversationId));
                        getConversation(conversationId, page);
                    } else {
                        MyApplication.getInstance().hideProgress(ChatActivity.this);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(ChatActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(ChatActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(ChatActivity.this);
            }
        });
    }





    private void getConversation(String id, int pageCounts)
    {
        if (!NetworkUtils.isNetworkAvailable(ChatActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), ChatActivity.this);
            return;
        }

        TinderAppInterface apiInterface = Injector.provideChatApi();
        MyApplication.getInstance().showProgress(ChatActivity.this, "", "");
        Observable<Response<ResponseBody>> api = apiInterface.getConversation(id, String.valueOf(pageCounts));

        Observable<Response<ResponseBody>> observeApi = api
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {

                    JSONObject jsonObject1 = generalResponse.response;
                    if (jsonObject1.getBoolean("status")) {

                        pageCount = Integer.parseInt(jsonObject1.getString("page_number")) + 1;
                        totalPages = jsonObject1.getInt("pages");

                        JSONArray jsonArrayForMessage = jsonObject1.getJSONArray("messages");
                        if (jsonArrayForMessage.length() != 0) {

                            for (int i = 0; i < jsonArrayForMessage.length(); i++) {

                                JSONObject jsonObjectMessage = jsonArrayForMessage.getJSONObject(i);
                                String title = "";
                                int viewType = 0;
                                MessageData messageData = new MessageData();
                                if (jsonObjectMessage.getString("sender").equals(senderId)) {
                                    title = "You";
                                    viewType = ListType.SENDER_VIEW_TYPE;
                                } else {
                                    title = user_name;
                                    viewType = ListType.RECEIVER_VIEW_TYPE;
                                }

                                messageData.setId(jsonObjectMessage.getString("id"));
                                messageData.setConversationId(jsonObjectMessage.getString("conversationId"));
                                messageData.setMessage(jsonObjectMessage.getString("message"));
                                messageData.setSender(jsonObjectMessage.getString("sender"));
                                messageData.setCreatedAt(jsonObjectMessage.getString("createdAt"));
                                messageData.setType(viewType);
                                messageData.setNameTitle(title);
                                messageDataList.add(messageData);
                            }

                            chatListAdapter = new ChatListAdapter(messageDataList, ChatActivity.this);
                            recycler.setAdapter(chatListAdapter);
                            scrollToBottom();

                        } else {

                        }

                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(ChatActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(ChatActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(ChatActivity.this);
            }
        });
    }
}
