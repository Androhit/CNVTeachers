package com.rjp.cnvteachers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rjp.cnvteachers.common.CommonFunctions;

/**
 * Created by Shraddha on 3/27/2017.
 */
public class ViewDocument extends AppCompatActivity{

    private String TAG = ViewDocument.class.getSimpleName();
    private Context mContext;

    private WebView wvDocument;
    private FloatingActionButton fab;
    private String FILE_URL = null;
    private String FILE_NAME = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_document);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initIntents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
                        //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                    } else {
                        CommonFunctions.downloadFile(mContext,FILE_URL,FILE_NAME);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initIntents()
    {
        FILE_URL = getIntent().getStringExtra("FILE_URL");
        if(FILE_URL!=null)
        {
            setTitle("Attachment");
            String url = FILE_URL;

            String []file = FILE_URL.split("/");
            FILE_NAME= file[file.length-1];

            final String googleDocsUrl = "http://docs.google.com/viewer?url=";

            wvDocument.getSettings().setJavaScriptEnabled(true);
            //mWebView.getSettings().setPluginsEnabled(true);
            wvDocument.setWebViewClient(new MyWebViewClient());

               /* wvDocument.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url){

                        view.loadUrl(url);
                        return false; // then it is not handled by default action
                    }
                });*/

            wvDocument.loadUrl((googleDocsUrl + url));
        }
        else
        {
            finish();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                CommonFunctions.downloadFile(mContext,FILE_URL,FILE_NAME);
            } else {
                Toast.makeText(mContext, "Until you grant the permission, we can not display the images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init()
    {
        wvDocument = (WebView)findViewById(R.id.wvDocument);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

}
