package com.rjp.cnvteachers.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.DivisionListAdapter;
import com.rjp.cnvteachers.adapters.HandsOnScienceListAdapter;
import com.rjp.cnvteachers.adapters.PdfCreater;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.AdmissionBean;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.beans.HandsOnScienceBeans;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/29/2017.
 */
public class HandsOnScienceFragment extends Fragment{

    private String TAG = "Hands on Science";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvHoscience;
    private SwipeRefreshLayout refreshView;
    private ArrayList<HandsOnScienceBeans> arrList = new ArrayList<HandsOnScienceBeans>();

    private EditText etSearch;
    private HandsOnScienceListAdapter adapt= null;
    private Spinner spnClass,spnDivision;
    private AutoCompleteTextView AutoName,AutoAdmno;
    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ArrayList<DivisonBean> arrDiv = new ArrayList<DivisonBean>();

    private ClassBean objClass;
    private DivisonBean objDiv;
    String Classid,Division,Name,Admno;
    private Button btSubmit;
    StudentBean objS;
    ClassBean objC;


    public static StudentBean objStud=null;
    public static AdmissionBean objStudAdm=null;
    private ArrayList<StudentBean> arrStud = new ArrayList<StudentBean>();
    private ArrayList<AdmissionBean> arrStudAdm = new ArrayList<AdmissionBean>();
    private FloatingActionButton fabpdf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hands_on_sci, container, false);
        mContext = getContext();

        initRetrofitClient();
        init(view);
        initData();
        initDataAdmno();
        initStudName();

        //getHosDataDataService();
        setListners();
        return view;
    }

    private void initStudName(){
        if(NetworkUtility.isOnline(mContext))
        {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
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
                                arrStud = apiResults.getStudent();
                                ArrayAdapter<StudentBean> adapter = new ArrayAdapter<StudentBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrStud);
                                AutoName.setThreshold(1);
                                AutoName.setAdapter(adapter);
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

        AutoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoName.showDropDown();
            }
        });

        AutoName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    }
                        if (apiResults != null) {
                            if (apiResults.getAdmno_no() != null) {
                                arrStudAdm = apiResults.getAdmno_no();
                                ArrayAdapter<AdmissionBean> adapter = new ArrayAdapter<AdmissionBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrStudAdm);
                                AutoAdmno.setThreshold(1);
                                AutoAdmno.setAdapter(adapter);
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

        AutoAdmno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoAdmno.showDropDown();
            }
        });

        AutoAdmno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                objStudAdm= (AdmissionBean) adapterView.getItemAtPosition(i);
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
        rvHoscience = (RecyclerView) v.findViewById(R.id.rvHandsOnSci);
        refreshView = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_view_task);
        refreshView.setColorSchemeResources(R.color.cyan_900,R.color.colorAccent,R.color.yellow_500,R.color.red_900);
        etSearch = (EditText)v.findViewById(R.id.etSearch);
        AutoAdmno=(AutoCompleteTextView)v.findViewById(R.id.etAdmno);
        AutoName=(AutoCompleteTextView)v.findViewById(R.id.etStudName);
        spnClass=(Spinner)v.findViewById(R.id.spnClass);
        spnDivision=(Spinner)v.findViewById(R.id.spnDivision);
        fabpdf=(FloatingActionButton) v.findViewById(R.id.fabpdf);
        btSubmit=(Button)v.findViewById(R.id.btSubmit);
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

    }



    private void setListners()
    {
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                setListners();
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

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Name = AutoName.getText().toString();
                Admno = AutoAdmno.getText().toString();

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


                objS=new StudentBean();
                objS.setName(Name);
                objS.setAdmno(Admno);
                objS.setDivision(Division);

                objC=new ClassBean();
                objC.setClasses(objClass.getclasses());

                if((!Classid.equals("0")) && (!Division.equals("Select Division")) || (Name.length()!=0) || (Admno.length()!=0))

                {

                   if (NetworkUtility.isOnline(mContext)) {
                    final ProgressDialog prog = new ProgressDialog(mContext);
                    prog.setMessage("Loading...");
                    prog.setCancelable(false);
                    prog.show();

                    String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                    String empid = AppPreferences.getLoginObj(mContext).getEmpid();
                    String acadyear = AppPreferences.getAcademicYear(mContext);


                    retrofitApi.getHandsonList(AppPreferences.getInstObj(mContext).getCode(),Name,Admno,Classid,Division,empid,br_id,acadyear, new Callback<ApiResults>() {
                        //retrofitApi.getHandsonList("hos_info",AppPreferences.getBranchId(mContext),AppPreferences.getAcademicYear(mContext),AppPreferences.getCurrentAdmno(mContext), new Callback<ApiResults>() {
                        @Override
                        public void success(ApiResults apiResultses, Response response) {

                            try {
                                refreshView.setRefreshing(false);
                                if (prog.isShowing()) {
                                    prog.dismiss();
                                }
                                fabpdf.setVisibility(View.VISIBLE);
                                arrList = apiResultses.getHos_info();
                                if (arrList != null) {

                                    if (arrList.size() > 0) {
                                        Log.e(TAG, "Size One Frag" + arrList.size());
                                        generateCurriculumList(arrList);
                                        getpdf();
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, "Error One Frag" + error);
                            if (prog.isShowing()) {
                                prog.dismiss();
                            }
                            refreshView.setRefreshing(false);
                            objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
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

    private void getpdf() {
        fabpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PdfCreater adapter = new PdfCreater(mContext);
                    adapter.create_pdf_handson(arrList,objS,objC);

                } catch (Exception e) {
                    e.printStackTrace();
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
                            objdiv.setDivision_name("Select Division\n" + "_______________");
                            arrDiv = apiResults.getDivison_list();
                            arrDiv.add(0, objdiv);
                           // ArrayAdapter<DivisonBean> adapter = new ArrayAdapter<DivisonBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrDiv);
                            DivisionListAdapter adapter=new DivisionListAdapter(getActivity(),R.layout.div_list_items,R.id.tvDiv,arrDiv);
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

    private void generateCurriculumList(ArrayList<HandsOnScienceBeans> arr) {
                try {
                    adapt = new HandsOnScienceListAdapter(mContext, arr);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    rvHoscience.setLayoutManager(mLayoutManager);
                    rvHoscience.setItemAnimator(new DefaultItemAnimator());
                    rvHoscience.setAdapter(adapt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
}



