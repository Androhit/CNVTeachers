package com.rjp.cnvteachers.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 5/5/2017.
 */

public class ManagementDashboard extends Fragment {


    private Context mContext;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String TAG="DashBoard";
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private TextView tvFromDate;
    private TextView tvToDate;
    private TextView tvPrese;
    private TextView tvAbse;
    private TextView TotalStud;
    private PieChart pieChartAtt;
    private BarChart barChartAtt;
    private BarChart barChartStud;
    private int DATE_DIALOG_ID=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mngmnt_dashboard, container, false);
        mContext = getContext();

        initRetrofitClient();
        init(v);
        initData();

        // configure pie barChart
        pieChartAtt.setUsePercentValues(true);
        pieChartAtt.setDescription(TAG);

        // enable hole and configure
        pieChartAtt.setDrawHoleEnabled(true);
        pieChartAtt.setHoleColorTransparent(true);
        pieChartAtt.setHoleRadius(7);
        pieChartAtt.setTransparentCircleRadius(10);

        // enable rotation of pie barChart by touch
        //pieChart.setRotationAngle(0);
        pieChartAtt.setRotationEnabled(false);
        pieChartAtt.setTouchEnabled(false);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay();
        setListener();
        return v;
    }

    private void initData() {
        if(NetworkUtility.isOnline(mContext))
        {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();


            retrofitApi.getTotStud(AppPreferences.getInstObj(mContext).getCode(), AppPreferences.getAcademicYear(mContext),new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();}
                    if (apiResults != null) {
                       TotalStud.setText(""+apiResults.getTotal_stud());
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


            retrofitApi.getClassStud(AppPreferences.getInstObj(mContext).getCode(), AppPreferences.getAcademicYear(mContext),new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();}
                    if (apiResults != null) {
                        StudentBean obj=new StudentBean();
                        obj=apiResults.getStudents_Count();
                        getDataSet(obj);
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

    private ArrayList<BarDataSet> getDataSet(StudentBean obj) {
        ArrayList<BarDataSet> dataSets = null;
        try {
            dataSets = null;

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            BarEntry v1e1 = null;

            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            BarEntry v2e1 = null;

            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            BarEntry v3e1 = null;

            int i,j;
            int s = obj.getClass_array().size();
            Log.e(TAG,"Size "+s);


            for (i = 1; i < 13; i++) {
                for (j=0;j<s;j++) {

                        v1e1 = new BarEntry(Long.valueOf(obj.getClass_array().get(j).getTOTAL()),i);
                        valueSet1.add(v1e1);
                        Log.e(TAG,"1 "+v1e1);
                  }
            }

            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Total Students ");
            barDataSet1.setColor(getResources().getColor(R.color.green_500));

            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);

            BarData data = new BarData(getXAxisValues(s,obj), dataSets);

            barChartStud.setData(data);
            barChartStud.setDescription("");
            barChartStud.animateXY(2000, 2000);
            barChartStud.setVisibleXRangeMaximum(12);
            barChartStud.invalidate();
            //  addData(arr);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e(TAG,"Number Error 1 "+e);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG,"Error1 "+e);
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.e(TAG,"Error 1"+e);
            e.printStackTrace();
        }

        return dataSets;
    }

    private List<String> getXAxisValues(int s,StudentBean obj) {
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=0;i<s;i++)
        {
            xAxis.add(i,obj.getClass_array().get(i).getClass_name());
        }
        return xAxis;
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

    private void setListener() {
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

    private void updateDisplay() {
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
    }

    private void init(View v) {
        objDialog = new ConfirmationDialogs(mContext);

        tvFromDate = (TextView)v.findViewById(R.id.tvAttendanceFromDate);
        tvToDate = (TextView)v.findViewById(R.id.tvAttendanceToDate);
        TotalStud = (TextView)v.findViewById(R.id.totalStud);
        tvPrese = (TextView)v.findViewById(R.id.tot_Present);
        tvAbse = (TextView)v.findViewById(R.id.tot_Absent);
        pieChartAtt = (PieChart) v.findViewById(R.id.pieChartAtt);
        barChartAtt = (BarChart) v.findViewById(R.id.barChartAtt);
        barChartStud = (BarChart) v.findViewById(R.id.barChartStud);

    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }
}
