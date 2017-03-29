package com.rjp.cnvteachers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.common.BlurBuilder;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.common.Validations;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private Context mContext = null;
    private API retrofitAPI;

    private EditText etUsername,etPassword;
    private Button btLogin;
    private CheckBox chkRemember;
    private TextView tvSchoolName;
    private ImageView ivCampusLogo;
    private RelativeLayout layMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        init();
        initRetrofitAPI();
        initData();
        if(AppPreferences.getLoginObj(mContext)!=null) {

            if (AppPreferences.getIsRemember(mContext)) {
                Intent intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        }

        //initBranchService();
        setListners();
        //showHintsFirstTime();
    }

    private void initData()
    {
        tvSchoolName.setText(""+AppPreferences.getInstObj(mContext).getName());
        if (android.os.Build.VERSION.SDK_INT >= 17)
        {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.school_student);
            Bitmap blurredBitmap = BlurBuilder.blur( mContext, icon);
            layMain.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );
        }
        Glide.with(mContext).load(""+AppPreferences.getInstObj(mContext).getLogo_link()).into(ivCampusLogo);
    }

    private void setListners()
    {

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try {
                    if(Validations.hasTextAvailable(etUsername))
                    {
                        if(Validations.hasTextAvailable(etPassword))
                        {
                            loginService();
                        }
                        else
                        {
                            etPassword.setError("Required");
                        }
                    }
                    else
                    {
                        etUsername.setError("Required");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginService()
    {
        try {
            if(NetworkUtility.isOnline(mContext))
            {
                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setMessage("Authenticating");
                dialog.show();
                // retrofitAPI.getLogin(objBranch.getBr_id(), etUsername.getText().toString(), etPassword.getText().toString(), new Callback<ApiResults>() {
                retrofitAPI.getLogin(AppPreferences.getInstObj(mContext).getCode(),"3", etUsername.getText().toString(), etPassword.getText().toString(), new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {

                            if (apiResults != null) {
                                if (apiResults.getLogin().isStatus()) {
                                    if (chkRemember.isChecked()) {
                                        AppPreferences.setIsRemember(mContext, true);
                                    } else {
                                        AppPreferences.setIsRemember(mContext, false);
                                    }

                                    AppPreferences.setLoginObj(mContext, apiResults.getLogin().getEmp_details());
                                    AppPreferences.setAcademicYear(mContext,apiResults.getLogin().getAcadyear());
                                    AppPreferences.setLoginObj(mContext, apiResults.getLogin().getEmp_details());
                                    //AppPreferences.setLoginObj(mContext, apiResults.getLogin());
                                    finish();
                                    startActivity(new Intent(mContext, HomeScreen.class));
                                    ConfirmationDialogs.successDialog(mContext,"Welcome : " + apiResults.getLogin().getEmp_details().getFirstname());
                                } else {
                                    ConfirmationDialogs.okDialog(mContext,"Authentication failed", "" + apiResults.getLogin().getResult());
                                }
                            }
                        } catch (Exception e) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
//                                    objDialog.okDialog("Error", "Server failuer occured. Please try again some times later " + error);
                        ConfirmationDialogs.serverFailuerDialog(mContext,"");
                    }
                });
//                        }
            }
            else
            {


                ConfirmationDialogs.noInternet(mContext,new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton()
                    {
                        loginService();
                    }


                    @Override
                    public void cancelButton() {
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRetrofitAPI()
    {
        RetrofitClient.initRetrofitClient();
        retrofitAPI = RetrofitClient.getRetrofitClient();
    }

    private void init()
    {
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btLogin = (Button)findViewById(R.id.btLogin);
        chkRemember = (CheckBox)findViewById(R.id.chkRemember);
        tvSchoolName = (TextView)findViewById(R.id.tvSchoolName);
        //spnBranch = (Spinner)findViewById(R.id.spnBranch);
        ivCampusLogo = (ImageView)findViewById(R.id.ivCampusLogo);
        //tvInstitute = (TextView)findViewById(R.id.tvInstitute);
        layMain = (RelativeLayout)findViewById(R.id.layMain);
    }
}
