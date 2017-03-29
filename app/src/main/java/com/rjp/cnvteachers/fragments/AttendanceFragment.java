package com.rjp.cnvteachers.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.common.ConfirmationDialogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shraddha on 3/21/2017.
 */

public class AttendanceFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private API retrofitApi;
    private Context mContext;
    private ConfirmationDialogs objDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        mContext = getContext();
        init(view);
        initRetrofitClient();
        loadFragments();
        return view;
    }

    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void init(View v)
    {
        objDialog = new ConfirmationDialogs(mContext);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
    }

    private void loadFragments()
    {
        //ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager()); // if parent is activitity
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()); // if parent is fragment
        adapter.addFrag(new AttendanceClassFragment(),"Classwise Attendance");
        adapter.addFrag(new AttendanceStudentFragment(),"Studentwise Attendance");
        //adapter.addFrag(new StudFeedbackFragment(),"FEEDBACK");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
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

}
