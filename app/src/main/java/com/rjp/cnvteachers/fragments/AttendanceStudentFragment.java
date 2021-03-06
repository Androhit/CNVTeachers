package com.rjp.cnvteachers.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.AdmissionBean;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.AttendanceBean;
import com.rjp.cnvteachers.beans.StudentBean;
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

public class AttendanceStudentFragment extends Fragment {

    private String TAG = "Studentwise Attendance";
    private Context mContext = null;
    private ConfirmationDialogs objDialog;
    private ArrayList<AttendanceBean> arrList;

    private TextView tvTotalDays;
    private TextView tvPrese,tvAbse,tvAtte,tvFromDate,tvToDate;
    private API retrofitApi;
    private AutoCompleteTextView etAdmno;
    private AutoCompleteTextView AutoName;
    private PieChart pieChart;
    private BarChart barChart;
    String Name,admno,FromDate,ToDate;

    ArrayList<AttendanceBean> arr = new ArrayList<>();

    private int mYear;
    private int mMonth;
    private int mDay;
    private int DATE_DIALOG_ID = 0;
    private Button btnSubmit;
    int Size;


    public static StudentBean objStud=null;
    public static AdmissionBean objStudAdm=null;
    private ArrayList<StudentBean> arrStud = new ArrayList<StudentBean>();
    private ArrayList<AdmissionBean> arrStudAdm = new ArrayList<AdmissionBean>();
    private AttendanceBean obj ;

    private LinearLayout laymain;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_att_stud, container, false);
        mContext = getContext();

        initRetrofitClient();
        init(v);

        initDataAdmno();
        initData();

        // configure pie barChart
        pieChart.setUsePercentValues(true);
        pieChart.setDescription(TAG);

        // enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(7);
        pieChart.setTransparentCircleRadius(10);

        // enable rotation of pie barChart by touch
        //pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay();
        setListners();
        return v;
    }

    private void initDataAdmno() {
        if(NetworkUtility.isOnline(mContext))
        {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();


            retrofitApi.getAdmno(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();}
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


    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void initData(){
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
                         prog.dismiss();}
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
                    initData();
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

    private void init(View v) {
         objDialog = new ConfirmationDialogs(mContext);

         AutoName=(AutoCompleteTextView) v.findViewById(R.id.etStudName);
         etAdmno=(AutoCompleteTextView)v.findViewById(R.id.etAdmno);
         tvFromDate = (TextView)v.findViewById(R.id.tvAttendanceFromDate);
         tvToDate = (TextView)v.findViewById(R.id.tvAttendanceToDate);
         tvTotalDays = (TextView)v.findViewById(R.id.tvTotalDays);
         tvPrese = (TextView)v.findViewById(R.id.tvPresentDays);
         tvAbse = (TextView)v.findViewById(R.id.tvAbsentDays);
         tvAtte = (TextView)v.findViewById(R.id.tvAttendance);
         btnSubmit=(Button) v.findViewById(R.id.btnSubmit);
         pieChart = (PieChart) v.findViewById(R.id.pieChart);
         barChart = (BarChart) v.findViewById(R.id.barChart);
         laymain=(LinearLayout) v.findViewById(R.id.laymain);
        }

    private void setListners() {
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Name = AutoName.getText().toString();
                admno = etAdmno.getText().toString();
                FromDate = tvFromDate.getText().toString();
                ToDate = tvToDate.getText().toString();

                if((Name.length()!=0) || (admno.length()!=0)) {
                    Log.e(TAG,"Name"+Name);
                    Log.e(TAG,"admno"+admno);
                    if ((!FromDate.equals(" From Date")) && (!ToDate.equals(" To Date"))) {
                        Log.e(TAG,"FromDate"+FromDate);
                        Log.e(TAG,"ToDate"+ToDate);
                        if (NetworkUtility.isOnline(mContext)) {

                            final ProgressDialog prog = new ProgressDialog(mContext);
                            prog.setMessage("Loading...");
                            prog.setCancelable(false);
                            prog.show();


                            String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                            String acadyear = AppPreferences.getAcademicYear(mContext);


                            retrofitApi.getAttendance(AppPreferences.getInstObj(mContext).getCode(), Name, admno, FromDate, ToDate, br_id, acadyear, new Callback<ApiResults>() {
                                @Override
                                public void success(ApiResults apiResults, Response response) {
                                    if (prog.isShowing()) {
                                        prog.dismiss();
                                    }
                                    if (apiResults != null) {
                                        obj = apiResults.getStud_att();
                                        AutoName.setText("" + obj.getName());
                                        etAdmno.setText("" + obj.getAdmno());
                                        tvTotalDays.setText("" + obj.getTot_working_days());
                                        tvFromDate.setText("" + obj.getFrom_date());
                                        tvToDate.setText("" + obj.getTo_date());
                                        tvPrese.setText("" + obj.getTot_present_day());
                                        tvAbse.setText("" + obj.getTot_absent_days());
                                        tvAtte.setText("" + obj.getTot_percent());

                                        addData(obj);
                                        getDataSet(obj);
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
                                    Log.e(TAG, "Json Error " + error);
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
                    } else {
                        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                        alert.setMessage(mContext.getResources().getString(R.string.error_input_field3));
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
                else {
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setMessage(mContext.getResources().getString(R.string.error_input_field0));
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

    private void addData(AttendanceBean obj)
    {
        try {
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();
            ArrayList<String> xVals = new ArrayList<String>();

            int i = 0;
            long present = 0;
            long absent = 0;
            long otOf = 0;

                present =  Long.valueOf(obj.getTot_present_day());
                otOf =  Long.valueOf(obj.getTot_working_days());
                absent=  Long.valueOf(obj.getTot_absent_days());


            yVals1.add(new Entry(present, 0));
            xVals.add("Present");

            yVals1.add(new Entry(absent, 0));
            xVals.add("Absent");

            Log.e(TAG,"Present Days "+present+" Out of "+otOf+" Absent "+absent);

            // create pie data set
            PieDataSet dataSet = new PieDataSet(yVals1, "Overall Percentage");
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);

            // add many colors
            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(getResources().getColor(R.color.green_500));
            colors.add(getResources().getColor(R.color.red_500));

            dataSet.setColors(colors);

            // instantiate pie data object now
            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            pieChart.setData(data);

            // undo all highlights
            pieChart.highlightValues(null);
            pieChart.animateXY(2000, 2000);

            // update pie barChart
            pieChart.invalidate();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e(TAG,"Number Error 2 "+e);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG,"Error 2"+e);
            e.printStackTrace();
        }
    }




    private ArrayList<BarDataSet> getDataSet(AttendanceBean obj)
    {
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
            int s = obj.getData_array().size();
            Log.e(TAG,"Size "+s);


            for (i = 1; i < 13; i++) {
                      for (j=0;j<s;j++) {
                           int m = Integer.parseInt(obj.getData_array().get(j).getMonth());
                          Log.e(TAG,"i val "+i);
                            Log.e(TAG,"Month "+m);

                            if (i == m) {
                                Log.e(TAG,"true match "+m);

                                v1e1 = new BarEntry(Long.valueOf(obj.getData_array().get(j).getPresent_day()), m-6);
                                valueSet1.add(v1e1);
                                Log.e(TAG,"1 "+v1e1);

                                v2e1 = new BarEntry(Long.valueOf(obj.getData_array().get(j).getAbsent_days()), m-6);
                                valueSet2.add(v2e1);
                                Log.e(TAG,"2 "+v2e1);

                                long total = Long.valueOf(obj.getData_array().get(j).getPresent_day()) + Long.valueOf(obj.getData_array().get(j).getAbsent_days());
                                v3e1 = new BarEntry(total, m-6);
                                valueSet3.add(v3e1);
                                Log.e(TAG,"3 "+v3e1);
                            }

                        }
                    }

            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Present ");
            barDataSet1.setColor(getResources().getColor(R.color.green_500));

            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Absent ");
            barDataSet2.setColor(getResources().getColor(R.color.red_500));

            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Working Days ");
            barDataSet3.setColor(getResources().getColor(R.color.orange_500));

            dataSets = new ArrayList<>();
            dataSets.add(barDataSet3);
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);

            BarData data = new BarData(getXAxisValues(), dataSets);

            barChart.setData(data);
            barChart.setDescription("");
            barChart.animateXY(2000, 2000);
            barChart.setVisibleXRangeMaximum(12);
            barChart.invalidate();
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

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        return xAxis;
    }
}
