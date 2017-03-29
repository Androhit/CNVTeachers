package com.rjp.cnvteachers.api;


import retrofit.Endpoint;

/**
 * Created by Rohit on 22/4/16.
 */
public class RetrofitEndPoint implements Endpoint
{
        private String url;

    public String getUrl() {
        return url;
    }

    @Override
    public String getName() {
        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    RetrofitEndPoint(String url)
    {
        this.url = url;
    }


}
