package com.rjp.cnvteachers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.CircularListAdapter;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.CircularBean;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/27/2017.
 */

public class CircularFragment extends Fragment {


    private String TAG = "Notice";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvNotice;
    private SwipeRefreshLayout refreshView;
    private EditText etSearch;
    private CircularListAdapter adapt= null;
    private Spinner spnClass;
    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ClassBean objClass;
    String Class="";
    private Button btSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circular, container, false);
        mContext = getContext();
        initRetrofitClient();
        init(view);
        initData();
        //getNoticeService();
        setListners();
        return view;
    }

    @Override
    public void onResume()
    {
     //   getNoticeService();
        super.onResume();
    }

    private void initData() {
        if (NetworkUtility.isOnline(mContext)) {

            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();

            retrofitApi.getClass_list(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    if (apiResults != null) {
                        if (apiResults.getClass_list() != null) {
                            ClassBean objclas = new ClassBean();
                            objclas.setClass_id("0");
                            objclas.setClasses("Select Class");
                            objclas.setDept_name("");
                            arrClass = apiResults.getClass_list();
                            arrClass.add(0, objclas);
                            //ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            ClassListAdapter adapter = new ClassListAdapter(getActivity(), R.layout.class_list_items, R.id.tvClass, arrClass);
                            spnClass.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
                }

            });
        }
    }

    private void setListners()
    {

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(mContext,"class not selected",Toast.LENGTH_LONG).show();

            }
        });

      /*  refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                getNoticeService();
            }
        });*/

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
                        adapt.filter(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btSubmit.setOnClickListener(new  View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getNoticeService();
            }
        });
    }

    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void getNoticeService()
    {
        try {
            if(NetworkUtility.isOnline(mContext))
            {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Loading...");
                prog.setCancelable(false);
                prog.show();
                refreshView.setRefreshing(true);

                if(objClass != null) {
                    Class = objClass.getClass_id();
                }

                retrofitApi.get_Circular( AppPreferences.getInstObj(mContext).getCode(),AppPreferences.getLoginObj(mContext).getBr_id(), AppPreferences.getAcademicYear(mContext), Class , new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response)
                    {
                        if(prog.isShowing())
                        {
                            prog.dismiss();
                        }
                        refreshView.setRefreshing(false);
                        ArrayList<CircularBean> arr = apiResults.getCircular_info();
                        if(arr!=null)
                        {
                            if(arr.size()>0)
                            {
                                generateNoticeList(arr);
                                AppPreferences.setCircularCount(mContext,0);
                            }
                            else
                            {
                                // Toast.makeText(mContext,"Data Not Found",Toast.LENGTH_LONG).show();
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
                        refreshView.setRefreshing(false);

                        if(prog.isShowing())
                        {
                            prog.dismiss();
                        }
                        Log.e(TAG,"Retrofit Error "+error);

                        objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
                    }
                });
            }
            else
            {
                objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton()
                    {
                        getNoticeService();
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

    private void generateNoticeList(ArrayList<CircularBean> arr) {
        try
        {
            adapt= new CircularListAdapter(mContext,arr);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvNotice.setLayoutManager(mLayoutManager);
            rvNotice.setItemAnimator(new DefaultItemAnimator());
            rvNotice.setAdapter(adapt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void init(View v)
    {
        objDialog = new ConfirmationDialogs(mContext);
        rvNotice = (RecyclerView)v.findViewById(R.id.rvNotice);
        refreshView = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_view_task);
        refreshView.setColorSchemeResources(R.color.cyan_900,R.color.colorAccent,R.color.yellow_500,R.color.red_900);
        etSearch = (EditText)v.findViewById(R.id.etSearch);
        spnClass = (Spinner)v.findViewById(R.id.spnClass);
        btSubmit=(Button)v.findViewById(R.id.btSubmit);
    }


}
