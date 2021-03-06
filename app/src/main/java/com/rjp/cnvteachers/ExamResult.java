package com.rjp.cnvteachers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rjp.cnvteachers.adapters.PdfCreater;
import com.rjp.cnvteachers.adapters.ResultListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ExamBean;
import com.rjp.cnvteachers.beans.ExamResultsBean;
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
    private ExamResultsBean obj;

    RecyclerView rvExamResults;
    TextView tvMarks,tvOutOff,tvGrade;
    private TextView tvPer;
    private FloatingActionButton fabpdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            Log.e(TAG,"Exam_name"+objExam.getExam_name());

            getExamDataService(objExam);
        }
        else
        {
            finish();
            Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();
        }
    }


    private void getExamDataService(final ExamBean objExam) {
        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("loading...");
            prog.setCancelable(false);
            prog.show();

            String exam_id= objExam.getExam_id();
            String admno=objExam.getAdmno();
            String acad_year=objExam.getAcad_year();

            retrofitApi.getExamResult(AppPreferences.getInstObj(mContext).getCode(), exam_id, admno ,acad_year, AppPreferences.getLoginObj(mContext).getBr_id(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                         prog.dismiss();
                    }
                    if(apiResults != null)
                    {

                        obj = apiResults.getStud_result();

                        if (obj != null) {
                            tvGrade.setText("" + obj.getTotal_grade());
                            tvMarks.setText("" + obj.getObtained_marks());
                            tvOutOff.setText("" + obj.getTotal_marks());
                            tvPer.setText("" + obj.getTotal_percentage());

                            getResult();
                            getpdf();
                        }
                        else {
                            obj = null;
                            Toast.makeText(mContext, "Result data not found", Toast.LENGTH_SHORT).show();
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

    private void getResult() {

        ArrayList<ExamResultsBean> arr = obj.getData_array();
        ResultListAdapter adapt = new ResultListAdapter(mContext,arr ,tvMarks,tvOutOff,tvPer,tvGrade);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvExamResults.setLayoutManager(mLayoutManager);
        rvExamResults.setItemAnimator(new DefaultItemAnimator());
        rvExamResults.setAdapter(adapt);
    }

    private void setListners() {
        getExamDataService(objExam);
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }


    private void getpdf() {
        fabpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PdfCreater adapter = new PdfCreater(mContext);
                    adapter.create_pdf_performance(obj.getData_array(),obj,objExam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init() {

        objDialog = new ConfirmationDialogs(mContext);

        rvExamResults = (RecyclerView) findViewById(R.id.rvExamResults);
        tvMarks = (TextView)findViewById(R.id.tvMarks);
        tvOutOff = (TextView)findViewById(R.id.tvOutOff);
        tvGrade = (TextView)findViewById(R.id.tvGrade);
        tvPer=(TextView)findViewById(R.id.tvPer);
        fabpdf=(FloatingActionButton) findViewById(R.id.fabpdf);

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
