package com.rjp.cnvteachers.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class PopupServiceBroadcast extends BroadcastReceiver
{
    private String TAG =PopupServiceBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent it = new Intent(context,NotificationPopupFloat.class);
        it.putExtra("title",intent.getStringExtra("title"));
        it.putExtra("body",intent.getStringExtra("body"));
        if(intent.getStringExtra("image")!=null) {
            it.putExtra("image", intent.getStringExtra("image"));
        }
        context.startService(it);
        Log.e(TAG,"Popup service starting "+intent.getStringExtra("body"));
    }
}
