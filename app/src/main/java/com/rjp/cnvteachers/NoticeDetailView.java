package com.rjp.cnvteachers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.CircularBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/27/2017.
 */
public class NoticeDetailView extends AppCompatActivity{

    private String TAG = NoticeDetailView.class.getSimpleName();
    private Context mContext = null;
    private API retrofitAPI;

    private WebView wvDetails;
    private Button btOk;
    private ProgressBar progressBar;
    private CircularBean objNotice = null;
    private ConfirmationDialogs objDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular_details);
        mContext = this;
        getSupportActionBar();
        init();
        initRetrofitClient();
        initIntents();
        initListners();
    }

    private void initListners()
    {
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }


    private void initIntents()
    {
        objNotice = (CircularBean) getIntent().getSerializableExtra("ObjNotice");
        if(objNotice!=null)
        {
            setTitle(""+objNotice.getCircular_title());
            getNoticeDetailsService(objNotice);
        }
        else
        {
            finish();
            Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();
        }
    }

    private void getNoticeDetailsService(final CircularBean obj)
    {
        if(NetworkUtility.isOnline(mContext))
        {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Fetching Details");
            prog.show();
            retrofitAPI.getNoticeDetails(AppPreferences.getInstObj(mContext).getCode(), AppPreferences.getLoginObj(mContext).getBr_id(), obj.getId(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response)
                {
                    if(prog.isShowing())
                    {
                        prog.dismiss();
                    }
                    ArrayList<CircularBean> arr = apiResults.getCircular_info_det();
                    if(arr!=null)
                    {
                        showInWebView(arr);
                    }
                    else
                    {
                        Log.e(TAG,"Data Null");
                        finish();
                        Toast.makeText(mContext,"Details not found for this notice",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if(prog.isShowing())
                    {
                        prog.dismiss();
                    }
                    Log.e(TAG,"Retro Err "+error);

                    objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));

                }


            });
        }
        else
        {
            Toast.makeText(mContext,"No Internet",Toast.LENGTH_LONG);
            finish();
        }
    }

    private void showInWebView(ArrayList<CircularBean> arr)
    {
        wvDetails.setWebViewClient(new myWebClient());
        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.loadDataWithBaseURL("", arr.get(0).getMsg_text(), "text/html", "UTF-8", "");
        wvDetails.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
        retrofitAPI = RetrofitClient.getRetrofitClient();
    }

    private void init()
    {
        wvDetails = (WebView)findViewById(R.id.wvDetails);
        btOk = (Button)findViewById(R.id.btOk);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
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
