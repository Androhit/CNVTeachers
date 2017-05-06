package com.rjp.cnvteachers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rjp.cnvteachers.ParentContacts;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.RecentChatsListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.FcmNotificationBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/28/2017.
 */

public class CommunicationFragment extends Fragment{
    public static boolean IS_REFRESH=false;
    private Context mContext;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvChatList;
    private EditText etSearch;
    private FloatingActionButton Adduser;
    private API retrofitApi;
    String TAG="Communication Fragment";
    private RecentChatsListAdapter adapt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        mContext = getContext();
        init(view);
        initRetrofitClient();
        getRecentChats();
        setListners();
        return view;
    }

    private void setListners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try {
                    if (charSequence.length()>0) {
                        String str = charSequence.toString();
                        adapt.filter(str);  //recent chat
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Wait...");
                prog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prog.dismiss();
                        Intent it = new Intent(mContext, ParentContacts.class);
                        //  it.putExtra("obj",obj);
                        mContext.startActivity(it);
                    }
                }, 500);
            }
        });
    }


    private void getRecentChats() {
        //do recent chat code here
        try {
            if(NetworkUtility.isOnline(mContext))
            {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Loading...");
                prog.setCancelable(false);
                prog.show();
                //refreshView.setRefreshing(true);
                retrofitApi.getRecentChats(AppPreferences.getInstObj(mContext).getCode(),AppPreferences.getLoginObj(mContext).getEmpid(),0,0,new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response)
                    {
                        if(prog.isShowing())
                        {
                            prog.dismiss();
                        }
                        //refreshView.setRefreshing(false);
                        ArrayList<FcmNotificationBean> arr = apiResults.getRecent_chats();
                        if(arr!=null)
                        {
                            if(arr.size()>0)
                            {
                                generateChatsList(arr);
                            }
                        }
                        else
                        {
                            // Toast.makeText(mContext,"Data Not Found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        //refreshView.setRefreshing(false);

                        if(prog.isShowing())
                        {
                            prog.dismiss();
                        }
                        Log.e(TAG,"Retrofit Error "+error);
                    }
                });
            }
            else
            {
                objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton()
                    {
                        getRecentChats();
                    }

                    @Override
                    public void cancelButton() {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generateChatsList(ArrayList<FcmNotificationBean> arr) {
        try
        {
            adapt= new RecentChatsListAdapter(mContext,arr);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvChatList.setLayoutManager(mLayoutManager);
            rvChatList.setItemAnimator(new DefaultItemAnimator());
            rvChatList.setAdapter(adapt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void init(View view) {

        objDialog = new ConfirmationDialogs(mContext);
        rvChatList = (RecyclerView)view.findViewById(R.id.rvChats);
        //refreshView = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_view_task);
        //refreshView.setColorSchemeResources(R.color.cyan_900,R.color.colorAccent,R.color.yellow_500,R.color.red_900);
        etSearch = (EditText)view.findViewById(R.id.etSearch);
        //fabAddUser = (FloatingActionButton)view.findViewById(R.id.fabAddUser);
        Adduser = (FloatingActionButton)view.findViewById(R.id.fabadd);
    }




}
