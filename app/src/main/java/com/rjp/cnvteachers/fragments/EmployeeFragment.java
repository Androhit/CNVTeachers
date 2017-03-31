package com.rjp.cnvteachers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.EmployeeBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/15/2017.
 */

public class EmployeeFragment extends Fragment {


    private String TAG="Employee Details";
    private Context mContext = null;
    private ConfirmationDialogs objDialog;
    private FloatingActionButton fabEdit;
    private EditText etEmpid,etBempid,etUserid,etAddr,etDoj, etDesignation, etQualification, etExp, etBloodGrp, etDob, etCaste, etReligion, etNationality, etEmerNo, etFatherName;
    private EditText etMiddleName, etEmail, etPermAddr, etName, etPhone;
    private RadioButton rbMale, rbFemale;
    private ImageView ivPic;
    private Menu mMenu;
    private API retrofitApi;
    public static EmployeeBean objEmp;
    String empid;
    private FloatingActionButton fabSave;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.employee_fragment, container, false);
        mContext = getContext();


        initRetrofitClient();
        init(v);
        initShowData();
        setListners();
        return v;
    }

    private void initShowData() {
        try {
            if (NetworkUtility.isOnline(mContext)) {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Loading...");
                prog.setCancelable(false);
                prog.show();

                empid = AppPreferences.getLoginObj(mContext).getEmpid();
                // String empid = "EMP43";

                retrofitApi.get_Employee_Profile(AppPreferences.getInstObj(mContext).getCode(),empid, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response) {
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                        if (apiResults != null) {

                            if (apiResults.getEmployee_data() != null) {
                                objEmp = apiResults.getEmployee_data();
                                etName.setText("" + objEmp.getName());
                                etEmpid.setText("" + objEmp.getEmpid());
                                etDesignation.setText("" + objEmp.getDesignation());
                                etQualification.setText("" + objEmp.getQualification());
                                etExp.setText("" + objEmp.getExperience());
                                etBloodGrp.setText("" + objEmp.getBgroup());
                                etDob.setText("" + objEmp.getBirthdate());

                                etReligion.setText("" + objEmp.getReligion());
                                etEmerNo.setText("" + objEmp.getEmg_phone());
                                etFatherName.setText("" + objEmp.getFather_name());
                                etMiddleName.setText("" + objEmp.getMiddle_name());
                                etEmail.setText("" + objEmp.getEmail());
                                etPermAddr.setText("" + objEmp.getPermanent_address());
                                etAddr.setText(""+objEmp.getAddress());
                                etDoj.setText(""+objEmp.getReg_date());
                                etBempid.setText(""+objEmp.getBiometric_empid());
                                etUserid.setText(""+objEmp.getUser_name());
                                etPhone.setText("" + objEmp.getPhone());
                                if (objEmp.getSex().equalsIgnoreCase("Male")) {
                                    rbMale.setChecked(true);
                                } else {
                                    rbFemale.setChecked(true);
                                }

                                Picasso.with(mContext).load("" + objEmp.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(ivPic);

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

        }
        catch (Exception e)
        {
            Log.e(TAG,"Retrofit empid "+empid);
        }
    }

    public void makeEditable() {
        fabEdit.setVisibility(View.GONE);

        //etDesignation.setFocusableInTouchMode(true);
        //etDesignation.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etEmerNo.setFocusableInTouchMode(true);
        etEmerNo.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etQualification.setFocusableInTouchMode(true);
        etQualification.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etExp.setFocusableInTouchMode(true);
        etExp.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etBloodGrp.setFocusableInTouchMode(true);
        etBloodGrp.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etDob.setFocusableInTouchMode(true);
        etDob.setBackgroundResource(R.drawable.square_corner_grey_edittext);


        etPermAddr.setFocusableInTouchMode(true);
        etPermAddr.setBackgroundResource(R.drawable.square_corner_grey_edittext);


        etReligion.setFocusableInTouchMode(true);
        etReligion.setBackgroundResource(R.drawable.square_corner_grey_edittext);

//        etFatherName.setFocusableInTouchMode(true);
  //      etFatherName.setBackgroundResource(R.drawable.square_corner_grey_edittext);

    //    etMiddleName.setFocusableInTouchMode(true);
//        etMiddleName.setBackgroundResource(R.drawable.square_corner_grey_edittext);

//        etEmail.setFocusableInTouchMode(true);
  //      etEmail.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etPermAddr.setFocusableInTouchMode(true);
        etPermAddr.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etAddr.setFocusableInTouchMode(true);
        etAddr.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etDoj.setFocusableInTouchMode(true);
        etDoj.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        etPhone.setFocusableInTouchMode(true);
        etPhone.setBackgroundResource(R.drawable.square_corner_grey_edittext);

        rbFemale.setEnabled(true);
        rbMale.setEnabled(true);

    }

   private void updateStudentInfoService()
   {

       fabSave.setVisibility(View.GONE);
        if (NetworkUtility.isOnline(mContext))
        {

           if (rbMale.isChecked()) {
                objEmp.setSex("Male");
            } else {
                objEmp.setSex("Female");
            }
            objEmp.setPermanent_address(etPermAddr.getText().toString());
            objEmp.setExperience(etExp.getText().toString());
           // objEmp.setDesignation(etDesignation.getText().toString());
            objEmp.setQualification(etQualification.getText().toString());
            objEmp.setEmg_phone(etBloodGrp.getText().toString());
            objEmp.setBirthdate(etDob.getText().toString());
            objEmp.setReg_date(etDoj.getText().toString());
            objEmp.setAddress(etAddr.getText().toString());
            objEmp.setPhone(etPhone.getText().toString());
            objEmp.setReligion(etReligion.getText().toString());
            objEmp.setEmg_phone(etEmerNo.getText().toString());

            String Empid=objEmp.getEmpid();
            String PermAddr= objEmp.getPermanent_address();
            String Exp=objEmp.getExperience();
           // String Designation=objEmp.getDesignation();
            String Qualification=objEmp.getQualification();
            String EmerNo=objEmp.getEmg_phone();
            String Bgroup=objEmp.getBgroup();
            String Dob=objEmp.getBirthdate();
            String Doj=objEmp.getReg_date();
            String Addr=objEmp.getAddress();
            String Sex=objEmp.getSex();
            String Phone= objEmp.getPhone();
            String Religion=objEmp.getReligion();

            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();


            retrofitApi.update_Employee_Profile(AppPreferences.getInstObj(mContext).getCode(),Empid, Exp,Qualification,EmerNo,Bgroup,Dob,Doj,Addr,Sex,Phone,Religion,PermAddr, new Callback<JsonElement>() {

                @Override
                public void success(JsonElement apiResults, Response response) {
                    if(prog.isShowing())
                    {
                        prog.dismiss();
                    }
                    if(apiResults != null)
                    {
                        if(apiResults.toString().contains("true"))
                        {

                            Toast.makeText(mContext,"Employee Record Updated Successfully",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(mContext,"No changes affected",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext,"Server Network Error 1",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    if(prog.isShowing())
                    {
                        prog.dismiss();
                    }
                    objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
                   // Toast.makeText(mContext,"Server Network Error 2",Toast.LENGTH_LONG).show();
                    Log.e(TAG,"Retrofit Error "+error);

                }
            });


        }

   }


    private void setListners() {


        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Wait...");
                prog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prog.dismiss();
                        makeEditable();
                    }
                }, 500);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Wait...");
                prog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prog.dismiss();
                        updateStudentInfoService();
                    }
                }, 500);
            }
        });

    }

    private void init(View v) {
        objDialog = new ConfirmationDialogs(mContext);
        fabEdit = (FloatingActionButton) v.findViewById(R.id.fab);
        fabSave= (FloatingActionButton) v.findViewById(R.id.fabSave);


        etEmpid= (EditText) v.findViewById(R.id.etEmpId);
        etDesignation = (EditText) v.findViewById(R.id.etDesignation);
        etQualification = (EditText) v.findViewById(R.id.etQualification);
        etExp = (EditText) v.findViewById(R.id.etExp);
        etBloodGrp = (EditText) v.findViewById(R.id.etBloodGrp);
        etDob = (EditText) v.findViewById(R.id.etDob);
        etReligion = (EditText) v.findViewById(R.id.etReligion);
        etEmerNo = (EditText) v.findViewById(R.id.etEmerNo);
        etFatherName = (EditText) v.findViewById(R.id.etFatherName);
        etMiddleName = (EditText) v.findViewById(R.id.etMiddleName);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPermAddr = (EditText) v.findViewById(R.id.etPerAddr);
        etPhone = (EditText) v.findViewById(R.id.etPhone);
        etName = (EditText) v.findViewById(R.id.etName);
        rbMale = (RadioButton) v.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) v.findViewById(R.id.rbFemale);
        etBempid=(EditText) v.findViewById(R.id.etBempid);
        etUserid=(EditText) v.findViewById(R.id.etUserid);
        etAddr=(EditText) v.findViewById(R.id.etAddr);
        etDoj=(EditText) v.findViewById(R.id.etDoj);

        fabSave.setVisibility(View.GONE);
        fabEdit.setVisibility(View.GONE);



        ivPic = (ImageView)v.findViewById(R.id.ivPic);
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }

}
