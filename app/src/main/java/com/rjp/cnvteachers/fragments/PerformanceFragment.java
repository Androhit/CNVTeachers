package com.rjp.cnvteachers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.ExamAdapter;
import com.rjp.cnvteachers.adapters.PerformanceAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.AcademicYearBean;
import com.rjp.cnvteachers.beans.AdmissionYearBean;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.beans.ExamBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/5/2017.
 */
public class PerformanceFragment extends Fragment
{
    private String TAG = "Performance Details";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;


    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ArrayList<DivisonBean> arrDiv = new ArrayList<DivisonBean>();
    private ArrayList<AcademicYearBean> arrAcad = new ArrayList<AcademicYearBean>();
    private ArrayList<AdmissionYearBean> arrAdm = new ArrayList<AdmissionYearBean>();
    private ArrayList<ExamBean> arrExam = new ArrayList<ExamBean>();

    private ArrayList arrSortBy= new ArrayList();
    private ArrayList arrSortOrder= new ArrayList();


    private Spinner spnAcadyr,spnAdmYr,spnExamType,spnExamName,spnClassName,spnDivision,spnSortBy,spnSortOrder;
    private RecyclerView rvPerform;
    private Button btSubmit;

    private ClassBean objClass;
    private DivisonBean objDiv;
    private AcademicYearBean objAcadYr;
    private AdmissionYearBean objAdmYr;
    private ExamBean objExam;


    String Division, Classid, AcadYr, AdmYr, ExamName, ExamType, SortOrder, SortBy;
    private PerformanceAdapter adapt;
    private EditText etSearch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_performance, container, false);
        mContext = getContext();


        initRetrofitClient();
        init(v);
        initData();
        setListners();
        return v;
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

        spnClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
                getdiv();
                getExamData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objDiv = (DivisonBean) parent.getItemAtPosition(position);
                getExamData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnAdmYr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objAdmYr = (AdmissionYearBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnAcadyr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objAcadYr = (AcademicYearBean) parent.getItemAtPosition(position);
                getExamData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spnExamName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objExam = (ExamBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtility.isOnline(mContext)) {
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


                    Division="";
                    if(objDiv != null && (!(objDiv.getDiv_id().equals("0")))) {
                        Division = objDiv.getDivision_name();
                    }
                    else if(objDiv.getDiv_id().equals("0"))
                    {
                        Division = "";
                    }


                    if(objAcadYr != null && (!(objAcadYr.getAcad_year().equals("Academic Year")))) {
                        AcadYr = objAcadYr.getAcad_year();
                    }

                    if(objAdmYr != null && (!(objAdmYr.getAdm_yr().equals("Admission Year")))) {
                        AdmYr = objAdmYr.getAdm_yr();
                    }

                    if(objExam != null && (!(objExam.getExam_id().equals("0")))) {
                        ExamName = objExam.getExam_id();
                    }

                    retrofitApi.getPerformance(AppPreferences.getInstObj(mContext).getCode(), Classid, Division, AppPreferences.getLoginObj(mContext).getBr_id(), AcadYr, AdmYr,ExamName, new Callback<ApiResults>() {
                        @Override
                        public void success(ApiResults apiResults, Response response) {
                            if(prog.isShowing())
                            {
                                prog.dismiss();
                            }

                            if(apiResults.getExam() != null)
                            {
                                arrExam=apiResults.getExam();
                                generateExamList();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if(prog.isShowing())
                            {
                                prog.dismiss();
                            }
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
                            objdiv.setDivision_name("Select Division");
                            arrDiv = apiResults.getDivison_list();
                            arrDiv.add(0, objdiv);
                            ArrayAdapter<DivisonBean> adapter = new ArrayAdapter<DivisonBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrDiv);
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


    private void generateExamList() {
        try
        {
            adapt= new PerformanceAdapter(mContext,arrExam);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvPerform.setLayoutManager(mLayoutManager);
            rvPerform.setItemAnimator(new DefaultItemAnimator());
            rvPerform.setAdapter(adapt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getExamData() {

        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();

            Classid = "0";

            if (objClass != null && (!(objClass.getClass_id().equals("0")))) {
                Classid = objClass.getClass_id();
            }


            Division = "";
            if (objDiv != null && (!(objDiv.getDiv_id().equals("0")))) {
                Division = objDiv.getDivision_name();
            }

            AcadYr = "";
            if (objAcadYr != null && (!(objAcadYr.getAcad_year().equals("0")))) {
                AcadYr = objAcadYr.getAcad_year();
            }

            retrofitApi.getexam_name(AppPreferences.getInstObj(mContext).getCode(), Classid,Division,AppPreferences.getLoginObj(mContext).getBr_id(), AcadYr,new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    if (apiResults != null) {
                        if (apiResults.getAll_exam_name() != null) {
                            ExamBean objExam = new ExamBean();
                            objExam.setExam_id("0");
                            objExam.setExam_name("Exam Name");
                            objExam.setExam_types("");
                            arrExam = apiResults.getAll_exam_name();
                            arrExam.add(0, objExam);
                            ExamAdapter adapter = new ExamAdapter(getActivity(), R.layout.exam_items, R.id.tvName, arrExam);
                            spnExamName.setAdapter(adapter);
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


            retrofitApi.getAcad_year(AppPreferences.getInstObj(mContext).getCode(),AppPreferences.getLoginObj(mContext).getBr_id(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    if (apiResults != null) {
                        if (apiResults.getAcad_yr_list() != null) {
                            AcademicYearBean objAcadyr = new AcademicYearBean();
                            objAcadyr.setAcad_year("Academic Year");
                            arrAcad = apiResults.getAcad_yr_list();
                            arrAcad.add(0, objAcadyr);
                            ArrayAdapter<AcademicYearBean> adapter = new ArrayAdapter<AcademicYearBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrAcad);
                            spnAcadyr.setAdapter(adapter);
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
            retrofitApi.getAdm_year(AppPreferences.getInstObj(mContext).getCode(),AppPreferences.getLoginObj(mContext).getBr_id(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    if (apiResults != null) {
                        if (apiResults.getAdm_yr_list() != null) {
                            AdmissionYearBean objAdmyr = new AdmissionYearBean();
                            objAdmyr.setAdm_yr("Admission Year");
                            arrAdm = apiResults.getAdm_yr_list();
                            arrAdm.add(0, objAdmyr);
                            ArrayAdapter<AdmissionYearBean> adapter = new ArrayAdapter<AdmissionYearBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrAdm);
                            spnAdmYr.setAdapter(adapter);
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

    private void init(View v) {
        objDialog = new ConfirmationDialogs(mContext);

        rvPerform = (RecyclerView) v.findViewById(R.id.rvPerform);

        spnClassName = (Spinner) v.findViewById(R.id.spnClass);
        spnDivision = (Spinner) v.findViewById(R.id.spnDivision);
        spnAcadyr = (Spinner) v.findViewById(R.id.spnAcadYr);
        spnAdmYr = (Spinner) v.findViewById(R.id.spnAdmYr);
        spnExamName = (Spinner) v.findViewById(R.id.spnExamName);
        etSearch = (EditText)v.findViewById(R.id.etSearch);
        btSubmit = (Button)v.findViewById(R.id.btSubmit);
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }


}
