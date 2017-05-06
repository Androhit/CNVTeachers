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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rjp.cnvteachers.NotebookCorrectionActivity;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.DivisionListAdapter;
import com.rjp.cnvteachers.adapters.StudentMultiSelectAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.beans.SubjectBean;
import com.rjp.cnvteachers.beans.TopicBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/26/2017.
 */

public class NotebookCorrectionFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private Context mContext;
    private int mYear;
    private int mMonth;
    private int mDay;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private TextView tvDate;
    private Button btnSubmit;
    private Spinner spnClassName;
    private Spinner spnTopic;
    private Spinner spnDivision;
    private ArrayList<ClassBean> arrClass;
    private DivisonBean objDiv;
    private int DATE_DIALOG_ID = 0;
    private ClassBean objClass;
    private Button btnSearch;
    private String Classid;
    private String Division;
    private StudentMultiSelectAdapter adapt;
    private ListView rvStud;
    private String Classname;
    private String Date;
    private ArrayList<DivisonBean> arrDiv;
    private ArrayList<SubjectBean> arrSub;
    private Spinner spnSubject;
    private SubjectBean objSub;
    private String Subjectid;
    private ArrayList<TopicBean> arrTopic;
    private TopicBean objTopic;
    private List<StudentBean> studList;
    private String Topicid;
    String chk="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notebookcorrection, container, false);
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

    private void initSubject() {

        Classid = "";
        if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
            Classid = objClass.getClass_id();
        }


        Division = "";
        if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
            Division = objDiv.getDivision_name();
        }

        if (NetworkUtility.isOnline(mContext)) {

            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();

            retrofitApi.getSubjectList(AppPreferences.getInstObj(mContext).getCode(), AppPreferences.getLoginObj(mContext).getEmpid(),Classid,Division,AppPreferences.getAcademicYear(mContext),AppPreferences.getLoginObj(mContext).getBr_id(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    if (apiResults != null) {
                        if (apiResults.getSubjects() != null) {
                            SubjectBean objSub = new SubjectBean();
                            objSub.setSub_id("0");
                            objSub.setSub_name("Select Subject");
                            arrSub = apiResults.getSubjects();
                            arrSub.add(0, objSub);
                            ArrayAdapter<SubjectBean> adapter = new ArrayAdapter<SubjectBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrSub);
                            spnSubject.setAdapter(adapter);
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

    private void initTopic() {

        Classid = "";
        if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
            Classid = objClass.getClass_id();
        }

        Subjectid = "";
        if (objSub != null || (!(objSub.getSub_id().equals("0")))) {
            Subjectid = objSub.getSub_id();
        }

        if (NetworkUtility.isOnline(mContext)) {

            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();

            retrofitApi.getTopic_list(AppPreferences.getInstObj(mContext).getCode(),Classid,Subjectid, new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    if (apiResults != null) {
                        if (apiResults.getTopics() != null) {
                            TopicBean objTopic = new TopicBean();
                            objTopic.setTopic_name("Select Topic");
                            arrTopic = apiResults.getTopics();
                            arrTopic.add(0, objTopic);
                            ArrayAdapter<TopicBean> adapter = new ArrayAdapter<TopicBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrTopic);
                           // ClassListAdapter adapter=new ClassListAdapter(getActivity(),R.layout.class_list_items,R.id.tvClass,arrTopic);
                            spnTopic.setAdapter(adapter);
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

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = dayOfMonth;
        mDay = dayOfMonth;
        updateDisplay();
    }

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
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field5));
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
                initSubject();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field7));
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
        spnSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objSub = (SubjectBean) parent.getItemAtPosition(position);
                initTopic();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field01));
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
        spnTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objTopic = (TopicBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field02));
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


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_student();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Classid = "";
                if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
                    Classid = objClass.getClass_id();
                    Classname = objClass.getclasses();
                }

                Division = "";
                if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
                    Division = objDiv.getDivision_name();
                }

                Subjectid = "";
                if (objSub != null || (!(objSub.getSub_id().equals("0")))) {
                    Subjectid = objSub.getSub_id();
                }

                Topicid = "";
                if (objTopic != null || (!(objTopic.getTopic_id().equals("0")))) {
                    Topicid = objTopic.getTopic_id();
                }

                Date = tvDate.getText().toString();

                studList = adapt.get_Student();

//                if(adapt!=null){
//                    for(String obj: studList)
//                    {
//                        chkId.append(obj);
//                        chkId.append(",");
//                    }
//                    Log.e(TAG,"ChkList"+chkId);
//                    chk=chkId.length() > 0 ? chkId.substring(0, chkId.length()-1) : "";
//                }

                if ((!Classid.equals("0")) && (!Division.equals("Select Division")) && (!Subjectid.equals("0")) &&  (!Topicid.equals("0")) )
                {
                    final ProgressDialog prog = new ProgressDialog(mContext);
                    prog.setMessage("Wait...");
                    prog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prog.dismiss();
                            Intent it = new Intent(mContext, NotebookCorrectionActivity.class);
                            it.putExtra("Class", Classid);
                            it.putExtra("Division", Division);
                            it.putExtra("Subjectid", Subjectid);
                            it.putExtra("Topicid", Topicid);
                            it.putExtra("Date", Date);
                            it.putExtra("chk", chk);
                         //   it.putExtra("arrStud",  studList);
                            it.putExtra("arrStud", (Serializable) studList);

                            mContext.startActivity(it);
                        }
                    }, 500);
                }
                else
                {
                    //objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_input_field1));
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setMessage(mContext.getResources().getString(R.string.error_input_field8));
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

    private void search_student() {
        Classid = "";
        if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
            Classid = objClass.getClass_id();
        }

        Division = "";
        if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
            Division = objDiv.getDivision_name();
        }

        if ((!Classid.equals("0")) && (!Division.equals("Select Division"))) {

            if (NetworkUtility.isOnline(mContext)) {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setTitle("Loading");
                prog.setMessage("Please wait");
                prog.show();

                retrofitApi.get_student(AppPreferences.getInstObj(mContext).getCode(), Classid, Division, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response) {
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                        btnSubmit.setVisibility(View.VISIBLE);
                        if (apiResults != null) {
                            int i,j;

                            if (apiResults.getStud().size() > 0) {

                                adapt = new StudentMultiSelectAdapter(mContext, apiResults.getStud());
                                rvStud.setAdapter(adapt);
                                rvStud.setVisibility(View.VISIBLE);
                                setListViewHeightBasedOnItems(rvStud);
                                rvStud.setAdapter(adapt);

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
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                    }
                });


            } else {
                objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton() {
                        search_student();
                    }

                    @Override
                    public void cancelButton() {

                    }
                });
            }

        }
        else
        {
            final AlertDialog alert = new AlertDialog.Builder(mContext).create();
            alert.setMessage(mContext.getResources().getString(R.string.error_input_field2));
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

    private boolean setListViewHeightBasedOnItems(ListView rvStud) {

        ListAdapter listAdapter = rvStud.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, rvStud);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = rvStud.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = rvStud.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            rvStud.setLayoutParams(params);
            rvStud.requestLayout();

            return true;

        } else {
            return false;
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

        tvDate = (TextView) v.findViewById(R.id.tvDate);
        btnSearch = (Button) v.findViewById(R.id.btSearch);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        spnClassName=(Spinner) v.findViewById(R.id.spnClassName);
        spnTopic=(Spinner) v.findViewById(R.id.spnTopic);
        spnSubject=(Spinner) v.findViewById(R.id.spnSubject);
        spnDivision=(Spinner) v.findViewById(R.id.tvDivision);
        rvStud=(ListView) v.findViewById(R.id.rvStud);
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }
}
