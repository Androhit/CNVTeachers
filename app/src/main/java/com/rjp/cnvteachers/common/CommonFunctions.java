package com.rjp.cnvteachers.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.ViewDocument;

/**
 * Created by Shraddha on 3/27/2017.
 */
public class CommonFunctions {

    public static String TAG = CommonFunctions.class.getSimpleName();

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

/*
    public static void downloadFile(final Context mContext, final String fileUrl, final String fileName)
    {
        try {
            final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+mContext.getResources().getString(R.string.app_name)+"/Documents";
            File fl = new File(dirPath+"/"+fileName);
            if(fl.exists())
            {
                viewAttachmentFile(mContext,dirPath+fileName);
                Toast.makeText(mContext,"Already downloaded this file", Toast.LENGTH_LONG).show();
            }
            else
            {
                File dir = new File(dirPath);
                if(!dir.isDirectory())
                {
                    dir.mkdirs();
                }

                Toast.makeText(mContext,"Download started", Toast.LENGTH_LONG).show();
                Uri downloadUri = Uri.parse(fileUrl);
                Log.e("AD","Uri "+downloadUri);
                final Uri destinationUri = Uri.parse(dirPath+"/"+fileName);
                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                        .addCustomHeader("Auth-Token", "YourTokenApiKey")
                        .setRetryPolicy(new DefaultRetryPolicy())
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                        //.setDownloadContext(downloadContextObject)//Optional
                        .setDownloadListener(new DownloadStatusListener() {
                            @Override
                            public void onDownloadComplete(int id)
                            {
                                Log.e(TAG,"Download Complete");
                                Toast.makeText(mContext,"File downloaded successfully in "+mContext.getResources().getString(R.string.app_name)+" folder.", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onDownloadFailed(int id, int errorCode, String errorMessage)
                            {
                                Log.e(TAG,"Download failed "+errorMessage);
                            }
                            @Override
                            public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                            }
                        });

                ThinDownloadManager downloadManager;
                downloadManager = new ThinDownloadManager();
                downloadManager.add(downloadRequest);

            }
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }*/



    public static void viewAttachmentFile(Context context, String path)
    {
        if(path.contains(".jpg") || path.contains(".png")|| path.contains(".jpeg"))
        {
            displayImage(context,path,"Image");
        }
        else
        {
            Intent it = new Intent(context, ViewDocument.class);
            it.putExtra("FILE_URL", path);
            it.putExtra("FILE_NAME", "Attachment");
            context.startActivity(it);
        }
    }

    public static void displayImage(final Context mContext, final String url, final String fileName)
    {
        View vw = View.inflate(mContext, R.layout.display_image_dialog,null);

        final AlertDialog alert = new AlertDialog.Builder(mContext).create();

        ImageView ivImage = (ImageView) vw.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView)vw.findViewById(R.id.tvTitle);
        TextView tvSave = (TextView)vw.findViewById(R.id.tvSaveToGallery);
        Button btClose = (Button)vw.findViewById(R.id.btClose);

        tvTitle.setText(fileName);

        if(url!=null) {
            if(url.length()>0) {
                Glide.with(mContext).load(url).placeholder(R.drawable.logo).into(ivImage);
            }
        }

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // downloadFile(mContext,url,fileName);
                alert.dismiss();
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
//        alert.setTitle("Login");
//        alert.setMessage("Login Successfully");
//        alert.setCancelable(false);
        alert.setView(vw);

        alert.show();
    }

}
