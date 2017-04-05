package com.rjp.cnvteachers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.rjp.cnvteachers.adapters.ResultListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
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
public class ExamResult extends AppCompatActivity{

    private String TAG = "Exam Tesult";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;

    private ExamBean objExam;
    private ArrayList<ExamResult> arrList = new ArrayList<ExamResult>();

    RecyclerView rvExamResults;
    TextView tvMarks,tvOutOff,tvGrade;
    private TextView tvPer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        mContext = this;
        getSupportActionBar();
        init();
        initRetrofitClient();
        initIntents();

        setListners();
    }

    private void initIntents() {
        objExam = (ExamBean) getIntent().getSerializableExtra("objExam");
        if(objExam!=null)
        {
            setTitle(""+objExam.getExam_name());
            getExamDataService(objExam);
        }
        else
        {
            finish();
            Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();
        }
    }

    private void getExamDataService(ExamBean objExam) {
        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("loading...");
            prog.setCancelable(false);
            prog.show();

            retrofitApi.getExamResult(AppPreferences.getInstObj(mContext).getCode(), AppPreferences.getLoginObj(mContext).getBr_id(), objExam.getExam_id(), objExam.getAdmno(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if(prog.isShowing())
                    {
                        prog.dismiss();
                    }

                    arrList=apiResults.getStud_result();

                    if(apiResults != null)
                    {
                        if (arrList.size() >0)
                        {
                            tvGrade.setText(""+apiResults.getTotal_grade());
                            tvMarks.setText(""+apiResults.getObtained_marks());
                            tvOutOff.setText(""+apiResults.getTotal_marks());
                            tvPer.setText(""+apiResults.getTotal_percentage());
                            ResultListAdapter adapt = new ResultListAdapter(mContext, arrList,tvMarks,tvOutOff,tvPer,tvGrade);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            rvExamResults.setLayoutManager(mLayoutManager);
                            rvExamResults.setItemAnimator(new DefaultItemAnimator());
                            rvExamResults.setAdapter(adapt);
                        }
                        else
                        {
                            arrList = null;
                            Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();

                        }
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

    }

    private void setListners() {

    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }

    private void init() {

        objDialog = new ConfirmationDialogs(mContext);

        rvExamResults = (RecyclerView) findViewById(R.id.rvExamResults);
        tvMarks = (TextView)findViewById(R.id.tvMarks);
        tvOutOff = (TextView)findViewById(R.id.tvOutOff);
        tvGrade = (TextView)findViewById(R.id.tvGrade);
        tvPer=(TextView)findViewById(R.id.tvPer);
    }

}
