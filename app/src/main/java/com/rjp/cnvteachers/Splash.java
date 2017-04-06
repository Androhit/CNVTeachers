package com.rjp.cnvteachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.common.Validations;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.net.URL;
import java.net.URLConnection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Splash extends AppCompatActivity {

    private String TAG = Splash.class.getSimpleName();
    private Context mContext;
    private API retrofitAPI;
    private ConfirmationDialogs objDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        initRetrofitAPI();
    // if server is connecting well

        checkConnection(RetrofitClient.ROOT_URL);
    }

    private void checkConnection(String url) {

        if(isConnectedToServer(url,2000)) {
            if (AppPreferences.getInstObj(mContext) != null) {
                Log.e(TAG, "Not Null");
                checkLogin();
            } else {
                Log.e(TAG, "Null");
                openValidation();
            }
        }
        else
        {
            // Alert - Server down
            ConfirmationDialogs.serverFailuerError(mContext, new ConfirmationDialogs.okCancel() {
                @Override
                public void okButton() {
                    checkConnection(RetrofitClient.ROOT_URL1);
                }

                @Override
                public void cancelButton() {
                    finish();
                }
            });

        }
    }


    private void checkLogin()
    {
        if(AppPreferences.getLoginObj(mContext)!=null)
        {
            if(AppPreferences.getIsRemember(mContext)) {
                finish();
                startActivity(new Intent(mContext, HomeScreen.class));
            }
            else
            {
                AppPreferences.setLoginObj(mContext,null);
                finish();
                startActivity(new Intent(mContext,HomeScreen.class));
            }
        }
        else
        {
            finish();
            startActivity(new Intent(mContext,LoginActivity.class));
        }
    }

    private void openValidation()
    {
        try {
            View vw = View.inflate(mContext, R.layout.institute_connect_dialog,null);

            final AlertDialog alert = new AlertDialog.Builder(mContext).create();

            Button btCancel= (Button)vw.findViewById(R.id.btExit);
            Button btConnect= (Button)vw.findViewById(R.id.btConnect);
            final EditText etLink = (EditText)vw.findViewById(R.id.etLink);
            alert.setView(vw);
            alert.setCancelable(false);
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                    System.exit(0);
                }
            });

            btConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                        if(Validations.hasTextAvailable(etLink))
                        {
                            alert.dismiss();
                            connectInstitute(etLink.getText().toString());
                    }
                }
            });

            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initRetrofitAPI()
    {
        RetrofitClient.initRetrofitClient();
        retrofitAPI = RetrofitClient.getRetrofitClient();
    }

    private void connectInstitute(final String code)
    {
        try {
            if(NetworkUtility.isOnline(mContext))
            {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setTitle("Validating app");
                prog.setMessage("Please wait...");
                prog.setCancelable(false);
                prog.show();
                retrofitAPI.validate_institute(code, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response)
                    {
                        try {
                            prog.dismiss();
                            if(apiResults!=null)
                            {
                                if(apiResults.getObj_institutes()!=null)
                                {
                                    AppPreferences.setInstObj(mContext,apiResults.getObj_institutes());
                                    checkLogin();
                                }
                                else
                                {
                                    ConfirmationDialogs.warningDialog(mContext,"Wrong app link,Please provide correct institute link.");
                                    openValidation();
                                }
                            }
                            else
                            {
                                ConfirmationDialogs.warningDialog(mContext,"Wrong app link,Please provide correct institute link.");
                                openValidation();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error)
                    {
                        prog.dismiss();
                        ConfirmationDialogs.serverFailuerDialog(mContext,"");

                        objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
                    }
                });
            }
            else
            {
                ConfirmationDialogs.noInternet(mContext, new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton() {
                        connectInstitute(code);
                    }

                    @Override
                    public void cancelButton() {
                        System.exit(0);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }


}
