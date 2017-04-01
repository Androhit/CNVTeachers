package com.rjp.cnvteachers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import com.rjp.cnvteachers.adapters.GalleryGridAdapter;
import com.rjp.cnvteachers.beans.EventsBean;
import com.rjp.cnvteachers.beans.GalleryBeans;
import com.rjp.cnvteachers.beans.HandsOnScienceBeans;
import com.rjp.cnvteachers.common.ConfirmationDialogs;

import java.util.ArrayList;

/**
 * Created by Shraddha on 3/29/2017.
 */
public class SchoolGallery extends AppCompatActivity{

    private String TAG = SchoolGallery.class.getSimpleName();
    private Context mContext = null;

    private ConfirmationDialogs objDialog;
    private GridView gvGallery;
    private SwipeRefreshLayout refreshView;
    private TextView tvEvent;
    private ArrayList<GalleryBeans> arrList = new ArrayList<GalleryBeans>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_gallery);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initIntents();
    }

    private void init()
    {
        gvGallery = (GridView)findViewById(R.id.gvGallery);
        tvEvent = (TextView)findViewById(R.id.tvEvent);
    }

    private void initIntents()
    {
        EventsBean objEvents = (EventsBean) getIntent().getSerializableExtra("ObjEvents");
        HandsOnScienceBeans objScience = (HandsOnScienceBeans) getIntent().getSerializableExtra("ObjScience");
        if(objEvents!=null) {
            if(objEvents.getEvent()!=null)
            {
                tvEvent.setText(""+objEvents.getEvent());
            }
            GalleryGridAdapter adapter = new GalleryGridAdapter(mContext, objEvents.getCount());
            gvGallery.setAdapter(adapter);
        }
        else if(objScience!=null) {
            if(objScience.getProj_aim()!=null)
            {
                tvEvent.setText(""+objScience.getProj_aim());
                setTitle(objScience.getProj_aim());
            }
            GalleryGridAdapter adapter = new GalleryGridAdapter(mContext, objScience.getCount());
            gvGallery.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
