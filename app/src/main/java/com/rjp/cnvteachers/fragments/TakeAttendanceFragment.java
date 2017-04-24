package com.rjp.cnvteachers.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.TakeAttendance;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.DivisionListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
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
 * Created by Shraddha on 4/24/2017.
 */

public class TakeAttendanceFragment extends Fragment implements DatePickerDialog.OnDateSetListener  {

    private Context mContext;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int DATE_DIALOG_ID = 0;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private TextView tvDate;
    private Button btTake;
    private Spinner spnClassName;
    private Spinner spnDivision;
    private ArrayList<ClassBean> arrClass;
    private DivisonBean objDiv;
    private ClassBean objClass;
    private String Classid;
    private ArrayList<DivisonBean> arrDiv;
    private String Division;
    private String AttDate;
    private String Classname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_take_att, container, false);
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

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    private void setListners() {

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DATE_DIALOG_ID = 0;
                DatePickerDialog dialog = new DatePickerDialog(mContext, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();
            }
        });

        spnClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
                getdiv();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field1));
                alert.setCancelable(false);

                alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                    }
                });

                alert.show();
            }
        });

        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objDiv = (DivisonBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field1));
                alert.setCancelable(false);

                alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                    }
                });

                alert.show();
            }
        });

        btTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Classid = "";
                if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
                    Classid = objClass.getClass_id();
                    Classname=objClass.getclasses();
                }

                Division = "";
                if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
                    Division = objDiv.getDivision_name();
                }


                AttDate = tvDate.getText().toString();


                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Wait...");
                prog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prog.dismiss();
                        Intent it = new Intent(mContext, TakeAttendance.class);
                        it.putExtra("Class",Classid);
                        it.putExtra("Classname",Classname);
                        it.putExtra("Division",Division);
                        it.putExtra("AttDate",AttDate);
                        mContext.startActivity(it);
                    }
                }, 500);
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
                            objdiv.setDivision_name("Select Division\n" + "_______________\n");
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

    private void updateDisplay() {
        tvDate.setText(new StringBuilder()                        // Month is 0 based so add 1
                .append(mYear).append("-")
                .append(mMonth + 1).append("-")
                .append(mDay).append(""));

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
                            //   ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            ClassListAdapter adapter=new ClassListAdapter(getActivity(),R.layout.class_list_items,R.id.tvClass,arrClass);
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
                    setListners();
                }

                @Override
                public void cancelButton() {

                }
            });
        }

    }

    private void init(View v) {
        objDialog = new ConfirmationDialogs(mContext);

        tvDate = (TextView) v.findViewById(R.id.tvAttendanceDate);
        btTake = (Button) v.findViewById(R.id.btSearch);
        spnClassName=(Spinner) v.findViewById(R.id.spnClassName);
        spnDivision=(Spinner) v.findViewById(R.id.tvDivision);

    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = dayOfMonth;
        mDay = dayOfMonth;
        updateDisplay();
    }
}
