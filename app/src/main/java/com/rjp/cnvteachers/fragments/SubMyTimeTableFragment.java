package com.rjp.cnvteachers.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.MyTimeTableAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.MyTimeTableBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;

import java.util.ArrayList;

/**
 * Created by Shraddha on 3/20/2017.
 */

public class SubMyTimeTableFragment extends Fragment {

    private String TAG = "School Calendar";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private String currentMonth ="";
    private RecyclerView rvcalendar;
    private ArrayList<MyTimeTableBean> arr = new ArrayList<MyTimeTableBean>();
    private MyTimeTableAdapter adapt = null;

    public SubMyTimeTableFragment()
    {
    }

    @SuppressLint("ValidFragment")
    public SubMyTimeTableFragment(MyTimeTableAdapter adapt)
    {
        this.adapt = adapt;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_calendar, container, false);
        mContext = getContext();
        init(view);
        initRetrofitClient();
        generateNoticeList();

        return view;
    }

    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void init(View view)
    {
        objDialog = new ConfirmationDialogs(mContext);
        rvcalendar = (RecyclerView)view.findViewById(R.id.rvCalendar);
    }

    private void generateNoticeList() {
        try
        {
            if(adapt!=null) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                rvcalendar.setLayoutManager(mLayoutManager);
                rvcalendar.setItemAnimator(new DefaultItemAnimator());
                rvcalendar.setAdapter(adapt);
            }
            else
            {
                Toast.makeText(mContext,"Null",Toast.LENGTH_SHORT).show();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



}
