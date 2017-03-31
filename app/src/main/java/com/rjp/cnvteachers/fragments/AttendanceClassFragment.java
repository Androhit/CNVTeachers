package com.rjp.cnvteachers.fragments;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.AttendanceClassFragmentAdapter;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.AttendanceBean;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/21/2017.
 */

public class AttendanceClassFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    private Context mContext;
    private TextView tvFromDate;
    private TextView tvToDate;
    private API retrofitApi;
    private RecyclerView rvSearchResult;
    private ConfirmationDialogs objDialog;
    private Button btSearch;
    private ArrayList<AttendanceBean> arrList;
    private AttendanceClassFragmentAdapter adapt;
    String FromDate,ToDate;
    private ClassBean objClass=null;
    private DivisonBean objDiv=null;


    private Spinner spnClassName;
    private Spinner spnDivision;
    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ArrayList<DivisonBean> arrDiv = new ArrayList<DivisonBean>();


    String Division;
    String Classid;

    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int DATE_DIALOG_ID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_att_class, container, false);
        mContext = getContext();

        initRetrofitClient();
        init(v);
        initData();

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        // display the current date
        updateDisplay();

        setListners();
        return v;
    }

    //return date picker dialog
//    @Override
//    protected Dialog showDialog(int id) {
//        switch (id) {
//            case DATE_DIALOG_ID:
//                return new DatePickerDialog(mContext, mDateSetListener, mYear, mMonth, mDay);
//        }
//        return null;
//    }

    //update month day year
    private void updateDisplay()
    {
        if(DATE_DIALOG_ID==0) {
            tvFromDate.setText(new StringBuilder()                        // Month is 0 based so add 1
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(""));
        }
        else
        {
            tvToDate.setText(new StringBuilder()                        // Month is 0 based so add 1
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(""));
        }

        //.append(mMonth + 1).append("-")
        //.append(mDay).append("-")
        //.append(mYear).append(" "));
    }

    // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

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
                         //   ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            ClassListAdapter adapter=new ClassListAdapter(getActivity(),R.layout.class_list_items,R.id.tvClass,arrClass);
                            spnClassName.setAdapter(adapter);
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
                            objdiv.setDivision_name("Select Division");
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
                    objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
                }
            });
        }
    }

    private void setListners() {


        spnClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Classid="";
            }
        });

        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objDiv = (DivisonBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Division="";
            }
        });


        btSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spnClassName != null && spnDivision != null && tvFromDate!=null && tvToDate!=null) {

                    if (NetworkUtility.isOnline(mContext)) {
                        final ProgressDialog prog = new ProgressDialog(mContext);
                        prog.setMessage("Loading...");
                        prog.setCancelable(false);

                        prog.show();

                        Classid = "";
                        if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
                            Classid = objClass.getClass_id();
                        }

                        Division = "";
                        if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
                            Division = objDiv.getDivision_name();
                        }


                        String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                        String acadyear = AppPreferences.getAcademicYear(mContext);

                        FromDate = tvFromDate.getText().toString();
                        ToDate = tvToDate.getText().toString();


                        retrofitApi.getClassAttendance(AppPreferences.getInstObj(mContext).getCode(), Classid, Division, br_id, acadyear, FromDate, ToDate, new Callback<ApiResults>() {
                            @Override
                            public void success(ApiResults apiResults, Response response) {


                                if (prog.isShowing()) {
                                    prog.dismiss();
                                }
                                if (apiResults.getClass_att() != null) {
                                    arrList = apiResults.getClass_att();
                                    generateList();

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
                                setListners();
                            }

                            @Override
                            public void cancelButton() {

                            }
                        });
                    }

                    }
               else
                    {
                        objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_input_field1));
                    }
            }
        });

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               DATE_DIALOG_ID = 0;
                DatePickerDialog dialog = new DatePickerDialog(mContext, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DATE_DIALOG_ID = 1;
                DatePickerDialog dialog = new DatePickerDialog(mContext, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();
            }
        });

    }


    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }

    private void init(View v) {

        objDialog = new ConfirmationDialogs(mContext);

        rvSearchResult = (RecyclerView) v.findViewById(R.id.rvSearchResult);
        tvFromDate = (TextView) v.findViewById(R.id.tvAttendanceFromDate);
        tvToDate = (TextView) v.findViewById(R.id.tvAttendanceToDate);
        btSearch = (Button) v.findViewById(R.id.btSearch);

        spnClassName=(Spinner) v.findViewById(R.id.spnClassName);
        spnDivision=(Spinner) v.findViewById(R.id.tvDivision);
    }

    private void generateList() {
        try {
            adapt = new AttendanceClassFragmentAdapter(mContext, arrList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvSearchResult.setLayoutManager(mLayoutManager);
            rvSearchResult.setItemAnimator(new DefaultItemAnimator());
            rvSearchResult.setAdapter(adapt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = dayOfMonth;
        mDay = dayOfMonth;
        updateDisplay();
    }
}