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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.AchievmentListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.AchievementsBean;
import com.rjp.cnvteachers.beans.AdmissionBean;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.common.Validations;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/29/2017.
 */
public class AchievmentFragment extends Fragment{
    private String TAG = AchievmentFragment.class.getSimpleName();
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvNotice;
    private SwipeRefreshLayout refreshView;
    private EditText etSearch;
    private AchievmentListAdapter adapt= null;
    String classid = "";
    private Button btnSubmit;
    private AutoCompleteTextView auto_StudName;
    private AutoCompleteTextView auto_admno;

    public static StudentBean objStud=null;
    public static AdmissionBean objStudAdm=null;
    private ArrayList<StudentBean> arrStud = new ArrayList<StudentBean>();
    private ArrayList<AdmissionBean> arrStudAdm = new ArrayList<AdmissionBean>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_good_news, container, false);
        mContext = getContext();
        initRetrofitClient();
        init(view);
        initData();
        initDataAdmno();
        setListners();
        return view;
    }

    @Override
    public void onResume()
    {
       // getGoodNewsService();
        super.onResume();
    }

    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void initData(){
        if(NetworkUtility.isOnline(mContext))
        {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("loading...");
            prog.setCancelable(false);
            prog.show();


            retrofitApi.getStudent(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                        if (apiResults != null) {
                            if (apiResults.getStudent() != null) {
                                arrStud = apiResults.getStudent();
                                ArrayAdapter<StudentBean> adapter = new ArrayAdapter<StudentBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrStud);
                                auto_StudName.setThreshold(1);
                                auto_StudName.setAdapter(adapter);
                            }
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
        else
        {
            objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                @Override
                public void okButton() {
                    initData();
                }

                @Override
                public void cancelButton() {

                }
            });
        }

        auto_StudName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_StudName.showDropDown();
            }
        });

        auto_StudName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                objStud= (StudentBean) adapterView.getItemAtPosition(i);
            }
        });
    }



    private void initDataAdmno() {
        if(NetworkUtility.isOnline(mContext))
        {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("loading...");
            prog.setCancelable(false);
            prog.show();


            retrofitApi.getAdmno(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                        if (apiResults != null) {
                            if (apiResults.getAdmno_no() != null) {
                                arrStudAdm = apiResults.getAdmno_no();
                                ArrayAdapter<AdmissionBean> adapter = new ArrayAdapter<AdmissionBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrStudAdm);
                                auto_admno.setThreshold(1);
                                auto_admno.setAdapter(adapter);
                            }
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
        else
        {
            objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                @Override
                public void okButton() {
                    initDataAdmno();
                }

                @Override
                public void cancelButton() {

                }
            });
        }

        auto_admno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_admno.showDropDown();
            }
        });

        auto_admno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                objStudAdm= (AdmissionBean) adapterView.getItemAtPosition(i);
            }
        });

    }


    private void setListners()
    {
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
              //  getGoodNewsService();
            }
        });

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


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Validations.hasText(auto_admno)  || Validations.hasText(auto_StudName)) {
                    if (NetworkUtility.isOnline(mContext)) {

                        final ProgressDialog prog = new ProgressDialog(mContext);
                        prog.setMessage("Loading...");
                        prog.setCancelable(false);
                        prog.show();

                        String Name = auto_StudName.getText().toString();
                        String admno = auto_admno.getText().toString();

                        String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                        String acadyear = AppPreferences.getAcademicYear(mContext);

                        refreshView.setRefreshing(true);

                        retrofitApi.getStudentAchievmentData(AppPreferences.getInstObj(mContext).getCode(), Name, admno, br_id, acadyear, new Callback<ApiResults>() {

                            @Override
                            public void success(ApiResults apiResults, Response response) {
                                if (prog.isShowing()) {
                                    prog.dismiss();
                                }
                                refreshView.setRefreshing(false);
                                ArrayList<AchievementsBean> arr = apiResults.getSpecial_achiv();
                                if (arr != null) {
                                    if (arr.size() > 0) {
                                        generateGoodNewsList(arr);
                                        AppPreferences.setAchievementCount(mContext, 0);
                                    } else {
                                        //Toast.makeText(mContext,"Data Not Found",Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    //Toast.makeText(mContext,"Data Not Found",Toast.LENGTH_LONG).show();
                                    //Toast.makeText(mContext,"Data Not Found",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                refreshView.setRefreshing(false);

                                if (prog.isShowing()) {
                                    prog.dismiss();
                                }
                                objDialog.okDialog("Error", mContext.getResources().getString(R.string.error_server_down));
                                Log.e(TAG, "Retrofit Error " + error);
                            }
                        });
                    } else {
                        objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                            @Override
                            public void okButton() {

                            }

                            @Override
                            public void cancelButton() {

                            }
                        });
                    }
                }
                else {
                    auto_admno.setError("Required");
                    auto_StudName.setError("Required");
                    objDialog.okDialog("Error", mContext.getResources().getString(R.string.error_input_field));
                }
            }
        });
    }

    private void init(View view)
    {
        objDialog = new ConfirmationDialogs(mContext);
        rvNotice = (RecyclerView)view.findViewById(R.id.rvGoodNews);
        refreshView = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_view_task);
        refreshView.setColorSchemeResources(R.color.cyan_900,R.color.colorAccent,R.color.yellow_500,R.color.red_900);
        etSearch = (EditText)view.findViewById(R.id.etSearch);
        auto_admno=(AutoCompleteTextView)view.findViewById(R.id.autoadmno);
        auto_StudName=(AutoCompleteTextView)view.findViewById(R.id.autoStudName);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);
    }

    private void generateGoodNewsList(ArrayList<AchievementsBean> arr) {
        try
        {
            adapt= new AchievmentListAdapter(mContext,arr);
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



}

