package com.rjp.cnvteachers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rjp.cnvteachers.adapters.StudentAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/24/2017.
 */

public class TakeAttendance extends AppCompatActivity{
    private Context mContext=null;
    private API retrofitApi;
    String Class,Division,AttDate;
    private ConfirmationDialogs objDialog;
    private ListView rvStud;
    private TextView tvclass,tvdiv,tvdate,tvempname;
    private Button btnok;
    private StudentAdapter adapt;
    private String Classname;
    private TextView tvMsg;
    private List<String> studList;
    private String TAG="Take Attendance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_students_dialog);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRetrofitClient();
        init();
        search_isTaken();
        initIntent();
        setListners();
    }

    private void setListners() {
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
            });

    }

    private void initIntent() {
        Class =  getIntent().getStringExtra("Class");
        Classname =  getIntent().getStringExtra("Classname");
        Division =  getIntent().getStringExtra("Division");
        AttDate =  getIntent().getStringExtra("AttDate");
        if ((!Class.equals("0")) && (!Division.equals("Select Division")) && (!AttDate.equals(" Attendance Date")))
        {
         //   setTitle(""+obj.getTitle());
            tvclass.setText(Classname);
            tvdiv.setText(Division);
            tvdate.setText(DateOperations.convertToddMMMyyyy(AttDate));
            search_isTaken();
            initData();
        }
        else
        {
            finish();
            Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();
        }
    }

    private void search_isTaken() {
        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setTitle("Loading");
            prog.setMessage("Please wait");
            prog.show();
            String br_id = AppPreferences.getLoginObj(mContext).getBr_id();

            retrofitApi.chk_attTaken(AppPreferences.getInstObj(mContext).getCode(), AttDate, Class, Division,br_id,AppPreferences.getAcademicYear(mContext), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    prog.dismiss();
                    if(apiResults!= null)
                    {
                        if(!apiResults.getEmpname().equals("false"))
                        {
                            tvempname.setVisibility(View.VISIBLE);
                            tvMsg.setVisibility(View.VISIBLE);
                            tvempname.setText(apiResults.getEmpname());
                            initData();
                            update();
                        }
                        else
                        {
                            initData();
                            insert();
                        }
                    }
                    else
                    {

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    prog.dismiss();
                }
            });

        }
    }

    private void insert() {


        StringBuilder chkId=new StringBuilder();
        String chk="";
        studList = adapt.get_Student();

        if(adapt!=null){
            for(String obj: studList)
            {
                chkId.append(obj);
                chkId.append(",");
            }
            Log.e(TAG,"ChkList"+chkId);
            chk=chkId.length() > 0 ? chkId.substring(0, chkId.length()-1) : "";
        }

        btnok.setVisibility(View.VISIBLE);


        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setTitle("Loading");
            prog.setMessage("Please wait");
            prog.show();
            String br_id = AppPreferences.getLoginObj(mContext).getBr_id();

            retrofitApi.insert_att(AppPreferences.getInstObj(mContext).getCode(),chk, AttDate, Class, Division, AppPreferences.getLoginObj(mContext).getEmpid(),br_id, AppPreferences.getAcademicYear(mContext), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    prog.dismiss();

                    if(apiResults!=null)
                    {
                        if(!apiResults.getResult().equals("false"))
                        {
                            objDialog.successDialog(mContext,"Attendance added successfully");
                            finish();
                            //makeDisabled();
                        }
                        else
                        {
                            Toast.makeText(mContext,"Error1 While inserting, Please check internet connectivity.",Toast.LENGTH_LONG).show();
                            Log.e(TAG,"Success"+apiResults.getResult());
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext,"Error2 While inserting, Please check internet connectivity.",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    prog.dismiss();
                }
            });

        }

    }

    private void update() {
        Toast.makeText(mContext,"Updating.........",Toast.LENGTH_LONG).show();

    }

    private void initData() {
        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setTitle("Loading");
            prog.setMessage("Please wait");
            prog.show();

            retrofitApi.get_student(AppPreferences.getInstObj(mContext).getCode(), Class, Division, new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    btnok.setVisibility(View.VISIBLE);
                    if (apiResults != null) {

                        if (apiResults.getStud().size() > 0) {
                            adapt = new StudentAdapter(mContext, apiResults.getStud());
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
                    initData();
                }

                @Override
                public void cancelButton() {

                }
            });
        }
    }

    private Boolean setListViewHeightBasedOnItems(ListView rvStud) {
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


    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void init() {
        objDialog = new ConfirmationDialogs(mContext);
        rvStud=(ListView) findViewById(R.id.rvStud);
        tvclass=(TextView) findViewById(R.id.tvClass);
        tvdiv=(TextView) findViewById(R.id.tvDiv);
        tvdate=(TextView) findViewById(R.id.tvAttendanceDate);
        tvempname=(TextView) findViewById(R.id.tvEmpName);
        tvMsg=(TextView) findViewById(R.id.tvMsg);
        btnok=(Button) findViewById(R.id.btnSubmit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
