package com.rjp.cnvteachers.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.DivisionListAdapter;
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
                            objclas.setClasses("----------------------------");
                            objclas.setDept_name("Select Class");
                            arrClass = apiResults.getClass_list();
                            arrClass.add(0, objclas);
                            //ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            ClassListAdapter adapter = new ClassListAdapter(getActivity(), R.layout.class_list_items, R.id.tvClass, arrClass);
                            spnClassName.setAdapter(adapter);
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

    private void getdiv() {

        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);

            prog.show();

            Classid = "";
            if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
                Classid = objClass.getClass_id();
            }

            retrofitApi.getDivison_list(AppPreferences.getInstObj(mContext).getCode(),Classid, new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    if (apiResults != null) {
                        if (apiResults.getDivison_list() != null) {
                            DivisonBean objdiv = new DivisonBean();
                            objdiv.setDiv_id("0");
                            objdiv.setDivision_name("Select Division\n_______________");
                            arrDiv = apiResults.getDivison_list();
                            arrDiv.add(0, objdiv);
                          //  ArrayAdapter<DivisonBean> adapter = new ArrayAdapter<DivisonBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrDiv);
                            DivisionListAdapter adapter = new DivisionListAdapter(getActivity(), R.layout.div_list_items, R.id.tvDiv, arrDiv);
                            spnDivision.setAdapter(adapter);
                        } else {
                            if (apiResults.getResult() != null) {
                                objDialog.okDialog("Error", apiResults.getResult());
                            }
                        }
                    } else {
                        if (apiResults.getResult() != null) {
                            objDialog.okDialog("Error", apiResults.getResult());
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    objDialog.okDialog("Error", mContext.getResources().getString(R.string.error_server_down));
                }
            });
        }
        else {
            objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                @Override
                public void okButton() {
                    getdiv();
                }

                @Override
                public void cancelButton() {

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
                getdiv();
               }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(mContext,"Please Select Class and Division !!", Toast.LENGTH_LONG).show();
                return;
            }
        });

        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              objDiv = (DivisonBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(mContext,"Please Select Class and Division !!", Toast.LENGTH_LONG).show();
                return;
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


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

                if(((!Classid.equals("0")) && (!Divisionid.equals("Select Division"))) || (StudName.length()!=0) || (Admno.length()!=0)) {

                    if (NetworkUtility.isOnline(mContext)) {
                        final ProgressDialog prog = new ProgressDialog(mContext);
                        prog.setMessage("Loading...");
                        prog.setCancelable(false);

                        prog.show();


                        retrofitApi.getStudent_list(AppPreferences.getInstObj(mContext).getCode(), StudName, Classid, Divisionid, Admno, new Callback<ApiResults>() {
                            @Override
                            public void success(ApiResults apiResults, Response response) {


                                if (prog.isShowing()) {
                                    prog.dismiss();
                                }
                                if (apiResults != null) {
                                    arrList = apiResults.getStudent_list();
                                    generateList();
                                } else {
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

                                objDialog.okDialog("Error", mContext.getResources().getString(R.string.error_server_down));
                            }
                        });
                    } else {
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
                else {
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setMessage(mContext.getResources().getString(R.string.error_input_field4));
                    alert.setCancelable(false);

                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alert.dismiss();
                        }
                    });

                    alert.show();
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
                    }
                        if (apiResults != null) {
                            if (apiResults.getAdmno_no() != null) {
                                arrStudAdm = apiResults.getAdmno_no();
                                ArrayAdapter<AdmissionBean> adapter = new ArrayAdapter<AdmissionBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrStudAdm);
                                etAdmno.setThreshold(1);
                                etAdmno.setAdapter(adapter);
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
                    }
                        if (apiResults != null) {
                            if (apiResults.getStudent() != null) {
                                arrList = apiResults.getStudent();
                                ArrayAdapter<StudentBean> adapter = new ArrayAdapter<StudentBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrList);
                                etStudName.setThreshold(1);
                                etStudName.setAdapter(adapter);
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
        else
        {
            objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                @Override
                public void okButton() {
                    initStudName();
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
