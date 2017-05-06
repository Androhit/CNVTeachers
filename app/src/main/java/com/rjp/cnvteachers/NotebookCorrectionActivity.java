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
import android.widget.Toast;

import com.rjp.cnvteachers.adapters.StudentRemarkAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.RemarkBean;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/26/2017.
 */

public class NotebookCorrectionActivity extends AppCompatActivity{

    private Context mContext;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvStud;
    private Button btnok;
    private String Classid,Division,Topicid,Subjectid,chk,Date;
    private StudentRemarkAdapter adapt;

    ArrayList<StudentBean> arr ;
    private String TAG="Remark Adapter";
    private HashMap<String,String> list;
    private ArrayList<RemarkBean> arrR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_students_remarks);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRetrofitClient();
        init();
        initIntent();
        initData();
        setListners();
    }

    private void initData() {
        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setTitle("Loading");
            prog.setMessage("Please wait");
            prog.show();

            retrofitApi.getRemark(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    if (apiResults != null) {

////                        String[] chkstudent = chk.split(",");
//
//                        ArrayList<String> arrAdmno = new ArrayList<String>();
//
                        StudentBean objStud= new StudentBean();
                       if (apiResults.getRemarks().size() > 0) {
                           Log.e(TAG,"Remarks Count "+apiResults.getRemarks().size());
                           Log.e(TAG,"Student Count "+arr.size());

                           RemarkBean rk= new RemarkBean();
                           rk.setRemark_id("0");
                           rk.setRemark("Select Remark");
                           rk.setValue("0");
                           arrR = apiResults.getRemarks();
                           arrR.add(0, rk);

                           adapt = new StudentRemarkAdapter(mContext, arrR,arr);
                           RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                           rvStud.setLayoutManager(mLayoutManager);
                           rvStud.setItemAnimator(new DefaultItemAnimator());
                           rvStud.setAdapter(adapt);

                        } else {
                            objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                @Override
                                public void okButton() {
                                    finish();
                                }

                                @Override
                                public void cancelButton() {
                                    finish();
                                }
                            });
                        }

                    } else {
                        objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                            @Override
                            public void okButton() {
                                finish();
                            }

                            @Override
                            public void cancelButton() {
                                finish();
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

    private void setListners() {

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list=adapt.getRemarkList();
                if(!list.isEmpty()) {

                    Log.e(TAG,"List "+list);

                    StringBuilder chkId=new StringBuilder();
                    String chk="";

                    Set set = list.entrySet();
                    Iterator i = set.iterator();

                    // Display elements
                    while(i.hasNext()) {
                        Map.Entry me = (Map.Entry)i.next();
                        chkId.append(me.getKey());
                        chkId.append("=");
                        chkId.append(me.getValue());
                        chkId.append(";");
                        System.out.print(me.getKey() + ": ");
                        System.out.println(me.getValue());
                    }
                    chk=chkId.length() > 0 ? chkId.substring(0, chkId.length()-1) : "";
                    Log.e(TAG,"ChkList"+chk);

                    if (NetworkUtility.isOnline(mContext)) {
                        final ProgressDialog prog = new ProgressDialog(mContext);
                        prog.setTitle("Loading");
                        prog.setMessage("Please wait");
                        prog.show();

                        retrofitApi.insert_Notebook(AppPreferences.getInstObj(mContext).getCode(),chk,Subjectid,AppPreferences.getLoginObj(mContext).getEmpid(),Classid,Division,Date,Topicid,AppPreferences.getLoginObj(mContext).getBr_id(),AppPreferences.getAcademicYear(mContext),new Callback<ApiResults>() {
                            @Override
                            public void success(ApiResults apiResults, Response response) {
                                if (prog.isShowing()) {
                                    prog.dismiss();
                                }

                                if(apiResults!=null)
                                {
                                    if(!apiResults.getResult().equals("false"))
                                    {
                                        objDialog.successDialog(mContext,"Notebook Correction added successfully");
                                        finish();
                                        //makeDisabled();
                                    }
                                    else
                                    {
                                        Toast.makeText(mContext,"Error1 While adding, Please check internet connectivity.",Toast.LENGTH_LONG).show();
                                        Log.e(TAG,"Success"+apiResults.getResult());
                                    }
                                }
                                else
                                {
                                    Toast.makeText(mContext,"Error2 While adding, Please check internet connectivity.",Toast.LENGTH_LONG).show();
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
                else {
                    objDialog.okDialog("Remark Error","Please select remark for student");
                }
            }
        });
    }

    private void initIntent() {
        Classid =  getIntent().getStringExtra("Class");
        Division =  getIntent().getStringExtra("Division");
        Subjectid =  getIntent().getStringExtra("Subjectid");
        Topicid =  getIntent().getStringExtra("Topicid");
        Date =  getIntent().getStringExtra("Date");
        arr = (ArrayList<StudentBean>) getIntent().getSerializableExtra("arrStud");
        Log.e(TAG,"Student arr "+arr);
        Log.e(TAG,"Topic id : "+Topicid);
    }

    private void init() {
        objDialog = new ConfirmationDialogs(mContext);
        rvStud=(RecyclerView) findViewById(R.id.rvStud);
        btnok=(Button) findViewById(R.id.btnSubmit);

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
