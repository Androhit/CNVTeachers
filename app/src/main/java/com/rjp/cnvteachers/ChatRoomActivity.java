package com.rjp.cnvteachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rjp.cnvteachers.adapters.ChatRoomThreadAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.app.Config;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.FcmNotificationBean;
import com.rjp.cnvteachers.beans.MessageTitlesBean;
import com.rjp.cnvteachers.beans.ParentBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.fragments.CommunicationFragment;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Shraddha on 4/28/2017.
 */

public class ChatRoomActivity extends AppCompatActivity{
    private String TAG = ChatRoomActivity.class.getSimpleName();
    private Context mContext;
    private ConfirmationDialogs objDialogs;
    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<FcmNotificationBean> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private AutoCompleteTextView inputTitle;
    private Button btnSend;
    private API retrofitAPI;
    EditText Title,Desc;

    public static boolean is_running = false;
    private MessageTitlesBean objTitles;
    private String RECEIVER_ID,RECEIVER_NAME;
    private int RECEIVER_TYPE,MSG_TYPE=0;
    private ProgressBar progMain;
    private FloatingActionButton fabaddtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        inputMessage = (EditText) findViewById(R.id.message);
        inputTitle = (AutoCompleteTextView) findViewById(R.id.autoTitle);
        btnSend = (Button) findViewById(R.id.btn_send);
        objDialogs = new ConfirmationDialogs(mContext);
        progMain = (ProgressBar)findViewById(R.id.progMain);
     //   fabaddtitle = (FloatingActionButton)findViewById(R.id.fabaddtitle);
//        Intent intent = getIntent();
//        chatRoomId = intent.getStringExtra("chat_room_id");
//        String title = intent.getStringExtra("name");

        //getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRetrofitAPI();

        /*if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }*/
        initIntents();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<>();

        // self user id is to identify the message owner
        //String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();
        String selfUserId = AppPreferences.getLoginObj(mContext).getEmpid();

        initMessagesList();
        initTitlesList();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendMessage();
                sendMessageRetrofit();
            }
        });


        //fetchChatThread();
    }

    private void addTitles() {

        View view = View.inflate(mContext,R.layout.add_title_details,null);
        final AlertDialog alert = new AlertDialog.Builder(mContext).create();

        Title = (EditText)view.findViewById(R.id.title);
        Desc = (EditText)view.findViewById(R.id.desc);
        Button btnok = (Button) view.findViewById(R.id.btnadd);
        Log.e(TAG,"IN Add Title "+Title);
        alert.setView(view);
        alert.show();

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtility.isOnline(mContext)) {
                    final ProgressDialog prog = new ProgressDialog(mContext);
                    prog.setMessage("loading...");
                    prog.setCancelable(false);
                    prog.show();

                    String T=Title.getText().toString();
                    String D=Desc.getText().toString();

                    if ((T.length() != 0) && (D.length() != 0)) {

                        retrofitAPI.addTitle(AppPreferences.getInstObj(mContext).getCode(),T,D, new Callback<ApiResults>() {
                            @Override
                            public void success(ApiResults apiResults, retrofit.client.Response response) {
                                prog.dismiss();
                                alert.dismiss();
                                initTitlesList();
                                if (apiResults.getResult().equals("true")) {
                                    CommunicationFragment.IS_REFRESH = true;
                                    Log.e(TAG, "title added successfully");
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                prog.dismiss();
                                alert.dismiss();
                                Log.e(TAG, "Error " + error);
                            }
                        });
                   }
                    else
                    {
                        final AlertDialog alert1 = new AlertDialog.Builder(mContext).create();
                        alert1.setMessage(mContext.getResources().getString(R.string.Error));
                        alert1.setCancelable(false);

                        alert1.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alert1.dismiss();
                            }
                        });
                        alert1.show();
                    }
                }
                else
                {
                    objDialogs.noInternet(new ConfirmationDialogs.okCancel()
                    {
                        @Override
                        public void okButton()
                        {
                            sendMessageRetrofit();
                        }
                        @Override
                        public void cancelButton() {

                        }
                    });
                }
            }
        });
    }


    private void initIntents() {
        ParentBean obj = (ParentBean) getIntent().getSerializableExtra("obj");
        FcmNotificationBean objChats = (FcmNotificationBean) getIntent().getSerializableExtra("objChats");
        if(obj!=null)
        {
            RECEIVER_ID = obj.getId();
            RECEIVER_TYPE = 1;
            RECEIVER_NAME = obj.getFathername()+" "+obj.getLastname();
            setTitle(RECEIVER_NAME);
            MSG_TYPE = 0;
        }
        else if(objChats!=null)
        {
            if(objChats.getSent_by()==1) {
                RECEIVER_ID = objChats.getReceiver_id();
                RECEIVER_TYPE = objChats.getReceiver_type();
                RECEIVER_NAME = objChats.getReceiver_name();
                MSG_TYPE = 0;
                setTitle(""+objChats.getReceiver_name());
            }
            else
            {
                RECEIVER_ID = objChats.getSender_id();
                RECEIVER_TYPE = objChats.getSender_type();
                RECEIVER_NAME = objChats.getSender_name();
                MSG_TYPE = 0;
                setTitle(""+objChats.getSender_name());
            }
        }

    }

    private void sendMessageRetrofit() {

        try {
            if(NetworkUtility.isOnline(mContext)) {
                final String input = this.inputMessage.getText().toString().trim();
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
                    return;
                }

                FcmNotificationBean message = new FcmNotificationBean();
                message.setSender_id(AppPreferences.getLoginObj(mContext).getEmpid());
                message.setSender_name(AppPreferences.getLoginObj(mContext).getFirstname()+" "+AppPreferences.getLoginObj(mContext).getLastname());
                message.setSender_type(0); // 0 ; teacher , 1 - parent
                message.setReceiver_id(RECEIVER_ID);
                message.setReceiver_name(RECEIVER_NAME);
                message.setReceiver_type(RECEIVER_TYPE);// 0 ; teacher , 1 - parent

                if(objTitles==null) {
                    Snackbar.make(inputTitle, "Select title from list", Snackbar.LENGTH_LONG).show();
                    return;
                }

                message.setTitle(objTitles.getTitle());
                message.setTitle_id(Integer.parseInt(objTitles.getChrDocNo()));
                message.setMessage(input);
                message.setMsg_type(0);// 0 - individual , 1 - group
                message.setEntry_date(DateOperations.currentDateTime());
                message.setApp_name(mContext.getResources().getString(R.string.app_name));

                messageArrayList.add(message);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() > 1) {
                    // scrolling to bottom of the recycler view
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                }

                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String placeJSON = gson.toJson(message);

                inputMessage.setText("");


                retrofitAPI.sendMessage(AppPreferences.getInstObj(mContext).getCode(),placeJSON, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, retrofit.client.Response response)
                    {
                        if(apiResults.getResult().equals("true"))
                        {
                            CommunicationFragment.IS_REFRESH = true;
                            Log.e(TAG, "Message sent");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Error " + error);
                    }
                });
            }
            else
            {
                objDialogs.noInternet(new ConfirmationDialogs.okCancel()
                {
                    @Override
                    public void okButton()
                    {
                        sendMessageRetrofit();
                    }
                    @Override
                    public void cancelButton() {

                    }
                });
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }



    private void initTitlesList() {
       // Log.e(TAG,"IN TITLE : "+"welcome");


            if(NetworkUtility.isOnline(mContext))
            {
                retrofitAPI.getTitleList(AppPreferences.getInstObj(mContext).getCode(),new Callback<ApiResults>() {

                    @Override
                    public void success(ApiResults apiResults, retrofit.client.Response response)
                    {
                        Log.e(TAG,"IN TITLE : "+"welcome");

                        if(apiResults!=null)
                        {
                            ArrayAdapter<MessageTitlesBean> arrayAdapter = new ArrayAdapter<MessageTitlesBean>(mContext,android.R.layout.simple_spinner_dropdown_item,apiResults.getTitles_list());
                            inputTitle.setThreshold(1);
                            inputTitle.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG,"not IN TITLE : "+error);

                    }
                });
            }
            else
            {
                objDialogs.noInternet(new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton()
                    {
                        initTitlesList();
                    }

                    @Override
                    public void cancelButton() {

                    }
                });
            }

            inputTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputTitle.showDropDown();
                }
            });

            inputTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    objTitles = (MessageTitlesBean) adapterView.getItemAtPosition(i);

                }
            });


    }

    private void initMessagesList() {
        try {
            if (NetworkUtility.isOnline(mContext)) {
                progMain.setVisibility(View.VISIBLE);
                retrofitAPI.getMessagesList(AppPreferences.getInstObj(mContext).getCode(),AppPreferences.getLoginObj(mContext).getEmpid(), 0, RECEIVER_ID, RECEIVER_TYPE, MSG_TYPE, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, retrofit.client.Response response) {
                        progMain.setVisibility(View.GONE);
                        if (apiResults != null) {
                            if (apiResults.getMessage_list() != null)
                                messageArrayList = apiResults.getMessage_list();
                            mAdapter = new ChatRoomThreadAdapter(mContext, messageArrayList, AppPreferences.getLoginObj(mContext).getEmpid());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progMain.setVisibility(View.GONE);
                    }
                });
            } else {
                objDialogs.noInternet(new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton() {
                        initMessagesList();
                    }

                    @Override
                    public void cancelButton() {

                    }
                });
            }
        } catch (Exception e) {
            progMain.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        is_running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        is_running = false;
    }

    private void initRetrofitAPI()
    {
        RetrofitClient.initRetrofitClient();
        retrofitAPI = RetrofitClient.getRetrofitClient();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

//        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     * */
    private void handlePushNotification(Intent intent) {
        FcmNotificationBean message = (FcmNotificationBean) intent.getSerializableExtra("obj");

        if (message != null) {
            messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addtitle_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else if (id == R.id.menu_addtitle)
        {
            // code
            addTitles();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
