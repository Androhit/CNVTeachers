package com.rjp.cnvteachers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.ExamListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.ExamBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/23/2017.
 */
public class ExamFragment extends Fragment {

    private String TAG = "Exam Time Table";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvExamTT;

    private ArrayList<ExamBean> arrList = new ArrayList<ExamBean>();
    private ExamBean objExam = null;
    private RecyclerView rvAcademics;
    private Spinner spnClass;
    private Button btSearch;

    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ClassBean objClass;
    String Classid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exam_results, container, false);

        mContext = getContext();

        init(v);
        initRetrofitClient();
        initData();
        setListners();
        return v;
    }


    private void setListners() {

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(NetworkUtility.isOnline(mContext)) {
                    final ProgressDialog prog = new ProgressDialog(mContext);
                    prog.setMessage("Loading...");
                    prog.setCancelable(false);

                    prog.show();

                    if(objClass != null && (!(objClass.getClass_id().equals("0")))) {
                        Classid = objClass.getClass_id();
                    }
                    else if(objClass.getClass_id().equals("0"))
                    {
                        Classid = "0";
                    }

                    String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                    String acadyear=AppPreferences.getAcademicYear(mContext);

                    retrofitApi.getExamInfo(AppPreferences.getInstObj(mContext).getCode(),  Classid,  br_id,acadyear,new Callback<ApiResults>() {
                        @Override
                        public void success(ApiResults apiResults, Response response) {
                            if (prog.isShowing()) {
                                prog.dismiss();
                            }
                            if (apiResults.getExam_list() != null) {
                                arrList = apiResults.getExam_list();
                                if (arrList != null) {
                                    if (arrList.size() > 0) {
                                        Log.e(TAG, "Size One Frag" + arrList.size());
                                        generateExamList(arrList);
                                    }
                                    else {
                                        objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                            @Override
                                            public void okButton() {
                                                setListners();
                                            }

                                            @Override
                                            public void cancelButton() {

                                            }
                                        });
                                    }
                                }
                                else {
                                    objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                        @Override
                                        public void okButton() {
                                            setListners();
                                        }

                                        @Override
                                        public void cancelButton() {

                                        }
                                    });
                                }
                            }
                            else {
                                objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                    @Override
                                    public void okButton() {
                                        setListners();
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
                            objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
                        }
                    });
                }
                else {
                    objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                        @Override
                        public void okButton() {
                            setListners();
                        }

                        @Override
                        public void cancelButton() {

                        }
                    });
                }
            }
        });
    }

    private void generateExamList(ArrayList<ExamBean> arr) {
        try {
            ExamListAdapter adapt = new ExamListAdapter(mContext, arr);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvAcademics.setLayoutManager(mLayoutManager);
            rvAcademics.setItemAnimator(new DefaultItemAnimator());
            rvAcademics.setAdapter(adapt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }

    private void init(View view) {

        objDialog = new ConfirmationDialogs(mContext);
        rvAcademics = (RecyclerView) view.findViewById(R.id.rvAcademics);
        spnClass = (Spinner) view.findViewById(R.id.spnClass);
        btSearch = (Button) view.findViewById(R.id.btSearch);
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
                            objclas.setClasses("All Class");
                            arrClass = apiResults.getClass_list();
                            arrClass.add(0, objclas);
                            objclas.setDept_name("");
                            //ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            ClassListAdapter adapter=new ClassListAdapter(getActivity(),R.layout.class_list_items,R.id.tvClass,arrClass);
                            spnClass.setAdapter(adapter);
                        }

                        else
                        {
                            if(apiResults.getResult()!=null)
                            {
                                objDialog.okDialog("Error",apiResults.getResult());
                            }
                        }
                    }
                    else
                    {
                        if(apiResults.getResult()!=null)
                        {
                            objDialog.okDialog("Error",apiResults.getResult());
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
        else {
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
    }
}
