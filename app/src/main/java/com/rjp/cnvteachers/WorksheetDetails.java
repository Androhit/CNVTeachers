package com.rjp.cnvteachers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rjp.cnvteachers.adapters.WorksheetStudentAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.beans.WorksheetBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/19/2017.
 */

public class WorksheetDetails extends AppCompatActivity{

    private String TAG = "WorkSheet";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;

    TextView tvTitle,tvDesc;
    Button btOk;
    RecyclerView rvStudList;
    private WorksheetBean obj;
    private ArrayList<StudentBean> arrList;
    private TextView tvstudents;
    private TextView tvFrom;
    private TextView tvTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_worksheet_dialog);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initRetrofitClient();
        initIntent();
        setListners();
    }

    private void initIntent() {
        obj = (WorksheetBean) getIntent().getSerializableExtra("ObjWorksheet");
        if(obj!=null)
        {
            setTitle(""+obj.getTitle());
            tvTitle.setText(obj.getTitle());
            tvDesc.setText(obj.getDesc());
            tvFrom.setText(DateOperations.convertToddMMMyyyy(obj.getFromDate()));
            tvTo.setText(obj.getToDate());

            initData(obj);
        }
        else
        {
            finish();
            Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();
        }
    }

    private void initData(WorksheetBean obj) {
        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("loading...");
            prog.setCancelable(false);
            prog.show();

            retrofitApi.get_studList(AppPreferences.getInstObj(mContext).getCode(), obj.getId(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {

                    prog.dismiss();

                    if(apiResults != null) {
                            arrList = apiResults.getStudList();
                        if (arrList.size() > 0) {
                            String s = String.valueOf(arrList.size());
                            Log.e(TAG,"arr size"+s);
                            tvstudents.setText(s);

                            WorksheetStudentAdapter adapt = new WorksheetStudentAdapter(mContext, arrList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            rvStudList.setLayoutManager(mLayoutManager);
                            rvStudList.setItemAnimator(new DefaultItemAnimator());
                            rvStudList.setAdapter(adapt);
                        }
                        else
                        {
                            tvstudents.setText("0");
                            objDialog.okDialog("Error",mContext.getResources().getString(R.string.NoStud));

                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    prog.dismiss();

                }

            });


        }

    }

    private void setListners() {
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void init() {
         tvTitle = (TextView) findViewById(R.id.tvTitle);
         tvFrom = (TextView) findViewById(R.id.from);
         tvTo = (TextView) findViewById(R.id.to);
         tvDesc = (TextView) findViewById(R.id.tvDetails);
         tvstudents = (TextView) findViewById(R.id.tvStudents);
         btOk = (Button) findViewById(R.id.btOk);
         rvStudList=(RecyclerView) findViewById(R.id.rvStudList);
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
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
