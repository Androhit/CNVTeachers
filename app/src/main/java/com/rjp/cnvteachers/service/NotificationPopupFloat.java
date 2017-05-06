package com.rjp.cnvteachers.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rjp.cnvteachers.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class NotificationPopupFloat extends Service {

    private WindowManager windowManager;
    private ImageView chatHead,ivExtraImage;
    WindowManager.LayoutParams params;
    private TextView tvTitle,tvDesc;
    private Button btOk;
    private View view;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            chatHead = new ImageView(this);
            chatHead.setImageResource(R.drawable.logo);

            view = View.inflate(getApplicationContext(), R.layout.notification_popup_dialog, null);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvDesc = (TextView) view.findViewById(R.id.tvDescription);
            ivExtraImage = (ImageView) view.findViewById(R.id.ivExtraPic);
            btOk = (Button)view.findViewById(R.id.btOk);

            params= new WindowManager.LayoutParams(
                    400,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.CENTER | Gravity.CENTER;
            //params.x = 0;
            //params.y = 100;

            //this code is for dragging the chat head
            chatHead.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(chatHead, params);
                            return true;
                    }
                    return false;
                }
            });
            windowManager.addView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null)
            windowManager.removeView(view);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public int onStartCommand (Intent intent, int flags, int startId){

        try {
            if(intent!=null) {
                String title = intent.getStringExtra("title");
                String body = intent.getStringExtra("body");
                String image = intent.getStringExtra("image");

                tvTitle.setText(""+title);
                tvDesc.setText(""+body);

                if(image!=null) {
                    if(image.length()>0) {
                        ivExtraImage.setVisibility(View.VISIBLE);
                        Glide.with(this).load(image).placeholder(R.drawable.logo).into(ivExtraImage);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }



    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}