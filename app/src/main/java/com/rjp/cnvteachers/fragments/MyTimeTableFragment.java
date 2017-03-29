package com.rjp.cnvteachers.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rjp.cnvteachers.R;

import com.rjp.cnvteachers.adapters.MyTimeTableAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;

import com.rjp.cnvteachers.beans.MyTimeTableBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/18/2017.
 */

public class MyTimeTableFragment extends Fragment {

    private String TAG = "My Time Table";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    private ArrayList<MyTimeTableBean> arrTimeTable = new ArrayList<MyTimeTableBean>();
    private ArrayList<MyTimeTableBean> arrMon = new ArrayList<MyTimeTableBean>();
    private ArrayList<MyTimeTableBean> arrTue = new ArrayList<MyTimeTableBean>();
    private ArrayList<MyTimeTableBean> arrWed = new ArrayList<MyTimeTableBean>();
    private ArrayList<MyTimeTableBean> arrThu = new ArrayList<MyTimeTableBean>();
    private ArrayList<MyTimeTableBean> arrFri = new ArrayList<MyTimeTableBean>();
    private ArrayList<MyTimeTableBean> arrSat = new ArrayList<MyTimeTableBean>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_timetable, container, false);

        mContext = getContext();

        initRetrofitClient();
        init(v);
        getMyTimeTableService();
        return v;
    }

    private void getMyTimeTableService() {
        if(NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();

            String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
            String acadyear=AppPreferences.getAcademicYear(mContext);
            String empid=AppPreferences.getLoginObj(mContext).getEmpid();

            retrofitApi.getMyTimeTable(AppPreferences.getInstObj(mContext).getCode(),empid, br_id, acadyear, new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    MyTimeTableBean obj = apiResults.getMy_timetable();

                    if (obj != null) {

                        arrMon = obj.getMon();
                        arrTue = obj.getTue();
                        arrWed = obj.getWed();
                        arrThu = obj.getThu();
                        arrFri = obj.getFri();
                        arrSat = obj.getSat();

                        Log.e(TAG, "Mon " + arrMon.size());
                        Log.e(TAG, "Tue " + arrTue.size());
                        Log.e(TAG, "wed " + arrWed.size());
                        Log.e(TAG, "Thu " + arrThu.size());
                        Log.e(TAG, "Fri " + arrFri.size());
                        Log.e(TAG, "Sat " + arrSat.size());
                        getList();

                    } else {
                        objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                            @Override
                            public void okButton() {
                                getMyTimeTableService();
                            }

                            @Override
                            public void cancelButton() {

                            }
                        });
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setTitle("Alert");
                    alert.setMessage("Time Table Not Loaded Completely");
                    alert.setCancelable(false);
                    Log.e(TAG, "Retrofit Error " + error);
                }
            });
        }

    }

    private void getList() {
        try {
            //ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager()); // if parent is Activity
            MyTimeTableFragment.ViewPagerAdapter adapter = new MyTimeTableFragment.ViewPagerAdapter(getChildFragmentManager()); // if parent is Fragment

            MyTimeTableAdapter adapt01= new MyTimeTableAdapter(mContext,arrMon);
            MyTimeTableAdapter adapt02= new MyTimeTableAdapter(mContext,arrTue);
            MyTimeTableAdapter adapt03= new MyTimeTableAdapter(mContext,arrWed);
            MyTimeTableAdapter adapt04= new MyTimeTableAdapter(mContext,arrThu);
            MyTimeTableAdapter adapt05= new MyTimeTableAdapter(mContext,arrFri);
            MyTimeTableAdapter adapt06= new MyTimeTableAdapter(mContext,arrSat);

            adapter.addFrag(new SubMyTimeTableFragment(adapt01),"Mon");
            adapter.addFrag(new SubMyTimeTableFragment(adapt02),"Tue");
            adapter.addFrag(new SubMyTimeTableFragment(adapt03),"Wed");
            adapter.addFrag(new SubMyTimeTableFragment(adapt04),"Thu");
            adapter.addFrag(new SubMyTimeTableFragment(adapt05),"Fri");
            adapter.addFrag(new SubMyTimeTableFragment(adapt06),"Sat");
            //adapter.addFrag(new StudFeedbackFragment(),"FEEDBACK");
            viewPager.setAdapter(adapter);
            Calendar cl = Calendar.getInstance();
            Log.e(TAG,"Week Cnt "+cl.get(Calendar.DAY_OF_WEEK));
            viewPager.setCurrentItem(cl.get(Calendar.DAY_OF_WEEK)-2);
            tabLayout.setupWithViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {


            return mFragmentTitleList.get(position);
        }
    }

    private void init(View v) {
        objDialog = new ConfirmationDialogs(mContext);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);

    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }

}
