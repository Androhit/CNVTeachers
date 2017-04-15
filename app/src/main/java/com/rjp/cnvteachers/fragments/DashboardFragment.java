package com.rjp.cnvteachers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rjp.cnvteachers.R;

/**
 * Created by Shraddha on 4/13/2017.
 */

public class DashboardFragment extends Fragment {
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mContext = getContext();

        initRetrofitClient();
        init(v);

        return v;
    }

    private void init(View v) {

    }

    private void initRetrofitClient() {

    }

}
