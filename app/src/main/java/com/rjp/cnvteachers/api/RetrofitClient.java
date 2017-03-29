package com.rjp.cnvteachers.api;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Rohit on 22/4/16.
 */
public class RetrofitClient
{
    private static API RETROFIT_CLIENT;
    private static String ROOT = null;
    private static OkHttpClient okHttpClient = null;
    private static RetrofitEndPoint retrofitEndPoint;
    //private static String ROOT_URL = "http://115.112.185.145/cvscloudcrm/android/";
    public static String ROOT_URL = "http://115.112.185.145/android_webservices/school_teacher_app/";
    //private static String ROOT_URL = "http://115.112.186.62/schoolems/mobandroid/";

    private RetrofitClient(){}

    public static API getRetrofitClient()
    {
        return RETROFIT_CLIENT;
    }

    public static void initRetrofitClient()
    {
        setupRetrofitClient();
    }

    private static void setupRetrofitClient()
    {
        retrofitEndPoint = new RetrofitEndPoint(ROOT_URL);
        okHttpClient = new OkHttpClient();

        okHttpClient.setReadTimeout(60, TimeUnit.MINUTES);
        okHttpClient.setWriteTimeout(60, TimeUnit.MINUTES);
        okHttpClient.setConnectTimeout(60, TimeUnit.MINUTES);
        okHttpClient.setFollowRedirects(false);

        RestAdapter builder = new RestAdapter.Builder()
                .setEndpoint(retrofitEndPoint)
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL).build();

        RETROFIT_CLIENT = builder.create(API.class);
    }

    public static void resetEndPoint()
    {
        retrofitEndPoint.setUrl(ROOT);
    }

    public static void setRetrofitEndPoint(String url)
    {
        retrofitEndPoint.setUrl(url);
    }

}
