package com.rjp.cnvteachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rjp.cnvteachers.adapters.ExamDetailsListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ExamBean;
import com.rjp.cnvteachers.beans.ExamDetailsBeans;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/24/2017.
 */
public class ExamTimeTable extends AppCompatActivity {
    private String TAG = "Exam Time Table";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvExamTT;
    private SwipeRefreshLayout refreshView;
    private ArrayList<ExamDetailsBeans> arrList = new ArrayList<ExamDetailsBeans>();
    private ExamBean objExam = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_time_table);
        getSupportActionBar();
        mContext = this;
        init();
        initRetrofitClient();
        initIntents();
        setListners();
    }

    private void initIntents()
    {
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

    private void setListners()
    {
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                getExamDataService(objExam);
            }
        });
    }

    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    private void init()
    {
        objDialog = new ConfirmationDialogs(mContext);
        rvExamTT = (RecyclerView)findViewById(R.id.rvExamTT);
        refreshView = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_view_task);
        refreshView.setColorSchemeResources(R.color.cyan_900,R.color.colorAccent,R.color.yellow_500,R.color.red_900);
    }

    private void getExamDataService(final ExamBean objExam)
    {
        try {
            if(NetworkUtility.isOnline(mContext)) {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("loading...");
                prog.setCancelable(false);
                prog.show();
                refreshView.setRefreshing(true);
                String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                retrofitApi.getExamDetailInfo(AppPreferences.getInstObj(mContext).getCode(),objExam.getExam_id(),br_id,AppPreferences.getAcademicYear(mContext), new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResultses, Response response) {
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                        arrList = apiResultses.getExamdetails_list();
                        refreshView.setRefreshing(false);
                        if (arrList != null) {

                            if (arrList.size() > 0) {
                                Log.e(TAG, "Size One Frag" + arrList.size());
                                generateExamList(arrList);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Error One Frag" + error);
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }

                        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                        alert.setTitle("Alert");
                        alert.setMessage("Server Network Error");
                        alert.show();
                        alert.setCancelable(false);
                        refreshView.setRefreshing(false);
                    }
                });
            }
            else
            {
                objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton()
                    {
                        getExamDataService(objExam);
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

    private void generateExamList(ArrayList<ExamDetailsBeans> arr) {
        try
        {
            ExamDetailsListAdapter adapt= new ExamDetailsListAdapter(mContext,arr);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvExamTT.setLayoutManager(mLayoutManager);
            rvExamTT.setItemAnimator(new DefaultItemAnimator());

            rvExamTT.setAdapter(adapt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
