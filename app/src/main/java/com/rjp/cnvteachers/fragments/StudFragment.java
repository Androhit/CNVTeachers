package com.rjp.cnvteachers.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.StudentListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.AdmissionBean;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StudFragment extends Fragment {

    private String TAG = "Student Details";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;

    private ArrayList<StudentBean> arrList = new ArrayList<StudentBean>();
    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ArrayList<DivisonBean> arrDiv = new ArrayList<DivisonBean>();


    public static StudentBean objStud=null;
    public static AdmissionBean objStudAdm=null;
    private ArrayList<StudentBean> arrStud = new ArrayList<StudentBean>();
    private ArrayList<AdmissionBean> arrStudAdm = new ArrayList<AdmissionBean>();


    private Button btSearch;

    private Spinner spnClassName;
    private Spinner spnDivision;
    private AutoCompleteTextView etAdmno,etStudName;
    private StudentListAdapter adapt= null;
    private RecyclerView rvStudList;
    private ClassBean objClass;
    private DivisonBean objDiv;
    String Divisionid;
    String Classid;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.student_fragment, container, false);
        mContext = getContext();


        initRetrofitClient();
        init(v);
        initDataAdmno();
        initStudName();
        initData();
        setListners();
        return v;
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
                            objclas.setDept_name("");
                            arrClass = apiResults.getClass_list();
                            arrClass.add(0, objclas);
                            //ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            ClassListAdapter adapter = new ClassListAdapter(getActivity(), R.layout.class_list_items, R.id.tvClass, arrClass);
                            spnClassName.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                    prog.dismiss();
                }

                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setTitle("Alert");
                    alert.setMessage("Server Network Error");
                    alert.show();
                    alert.setCancelable(true);
                }

            });

            retrofitApi.getDivison_list(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    if (apiResults != null) {
                        if (apiResults.getDivison_list() != null) {
                            DivisonBean objdiv = new DivisonBean();
                            objdiv.setDiv_id("0");
                            objdiv.setDivision_name("All Division");
                            arrDiv = apiResults.getDivison_list();
                            arrDiv.add(0, objdiv);
                            ArrayAdapter<DivisonBean> adapter = new ArrayAdapter<DivisonBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrDiv);
                            spnDivision.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setTitle("Alert");
                    alert.setMessage("Server Network Error");
                    alert.show();
                    alert.setCancelable(true);
                }
            });
        }
    }
    private void setListners()
    {

        spnClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
               }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              objDiv = (DivisonBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Divisionid="";
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

                        String StudName = etStudName.getText().toString();
                        //String ClassName = spnClassName.getOnItemSelectedListener().toString();


                        if(objClass != null && (!(objClass.getClass_id().equals("0")))) {
                            Classid = objClass.getClass_id();
                        }
                        else if(objClass.getClass_id().equals("0"))
                        {
                            Classid = "0";
                        }


                        Divisionid="";
                        if(objDiv != null && (!(objDiv.getDiv_id().equals("0")))) {
                            Divisionid = objDiv.getDivision_name();
                        }
                        else if(objDiv.getDiv_id().equals("0"))
                        {
                            Divisionid = "";
                        }

                        String Admno = etAdmno.getText().toString();

                         retrofitApi.getStudent_list(AppPreferences.getInstObj(mContext).getCode(), StudName, Classid, Divisionid, Admno, new Callback<ApiResults>() {
                                    @Override
                                    public void success(ApiResults apiResults, Response response) {


                                        if (prog.isShowing()) {
                                            prog.dismiss();
                                        }
                                        if (apiResults.getStudent_list() != null) {
                                            arrList = apiResults.getStudent_list();
                                            generateList();
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {

                                        if (prog.isShowing()) {
                                            prog.dismiss();
                                        }

                                        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                                        alert.setTitle("Alert");
                                        alert.setMessage("Server Network Error");
                                        alert.show();
                                        alert.setCancelable(true);
                                    }
                                });
                    }
            }
        });
    }

   private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }


    private void init(View v)
    {
        objDialog = new ConfirmationDialogs(mContext);
        rvStudList = (RecyclerView) v.findViewById(R.id.rvStudList);
        etStudName = (AutoCompleteTextView) v.findViewById(R.id.tvStudName);
        spnClassName = (Spinner) v.findViewById(R.id.spnClassName);
        spnDivision = (Spinner) v.findViewById(R.id.tvDivision);
        etAdmno = (AutoCompleteTextView) v.findViewById(R.id.tvAdmno);
        btSearch = (Button)v.findViewById(R.id.btSearch);
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
                                etAdmno.setThreshold(1);
                                etAdmno.setAdapter(adapter);
                            }
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setTitle("Alert");
                    alert.setMessage("Server Network Error");
                    alert.show();
                    alert.setCancelable(true);
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

        etAdmno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAdmno.showDropDown();
            }
        });

        etAdmno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                objStudAdm= (AdmissionBean) adapterView.getItemAtPosition(i);
            }
        });

    }

    private void initStudName(){
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
                                etStudName.setThreshold(1);
                                etStudName.setAdapter(adapter);
                            }
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setTitle("Alert");
                    alert.setMessage("Server Network Error");
                    alert.show();
                    alert.setCancelable(true);
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

        etStudName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etStudName.showDropDown();
            }
        });

        etStudName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                objStud= (StudentBean) adapterView.getItemAtPosition(i);
            }
        });
    }

    private void generateList() {
        try
        {
            adapt= new StudentListAdapter(mContext,arrList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvStudList.setLayoutManager(mLayoutManager);
            rvStudList.setItemAnimator(new DefaultItemAnimator());
            rvStudList.setAdapter(adapt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
